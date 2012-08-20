package net.imglib2.ops.operation.unary.img;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.ops.UnaryOutputOperation;
import net.imglib2.type.numeric.RealType;

/**
 * Operation to merge several intervals and their content to a resulting Img. If
 * intervals intersects, the interval with a higher offset will override the
 * interval with lower offset. Gaps between intervals will be filled with
 * emptyType.
 * 
 * This operation is mostly designed to compute rectangular subsets of images
 * and merge them back again.
 * 
 * 
 * @author dietzc, hornm University of Konstanz
 * 
 */
public final class MergeIterableIntervals< T extends RealType< T >> implements UnaryOutputOperation< IterableInterval< T >[], Img< T >>
{

	/* Factory to produce res img */
	private final ImgFactory< T > m_factory;

	/*
	 * True if dimensions of size one should be removed from resulting interval
	 */
	private final boolean m_adjustDimensionality;

	private ArrayList< Integer > m_invalidDims;

	/**
	 * @param emptyType
	 *            type with value to fill gaps between intervals
	 * @param factory
	 *            factory to produce the resulting img
	 */
	public MergeIterableIntervals( ImgFactory< T > factory, boolean adjustDimensionality )
	{
		m_factory = factory;
		m_adjustDimensionality = adjustDimensionality;
	}

	@Override
	public final Img< T > createEmptyOutput( final IterableInterval< T >[] src )
	{
		long[] resDims = initConstants( src );

		return m_factory.create( resDims, src[ 0 ].firstElement().createVariable() );
	}

	private long[] initConstants( final IterableInterval< T >[] src )
	{
		int numMaxDims = 0;

		for ( IterableInterval< T > interval : src )
		{
			numMaxDims = Math.max( interval.numDimensions(), numMaxDims );
		}
		@SuppressWarnings( "unchecked" )
		java.util.Set< Long >[] setDims = new HashSet[ numMaxDims ];

		for ( int s = 0; s < setDims.length; s++ )
		{
			setDims[ s ] = new HashSet< Long >();
		}

		for ( IterableInterval< T > interval : src )
		{
			for ( int d = 0; d < interval.numDimensions(); d++ )
			{
				for ( long i = interval.min( d ); i <= interval.max( d ); i++ )
					setDims[ d ].add( i );
			}
		}

		m_invalidDims = new ArrayList< Integer >();
		if ( m_adjustDimensionality )
		{
			for ( int d = 0; d < setDims.length; d++ )
			{
				if ( setDims[ d ].size() == 1 )
				{
					m_invalidDims.add( d );
				}
			}
		}

		long[] resDims = new long[ numMaxDims - m_invalidDims.size() ];
		int k = 0;
		for ( int d = 0; d < numMaxDims; d++ )
		{
			if ( m_invalidDims.contains( d ) )
				continue;

			resDims[ k++ ] = setDims[ d ].size();
		}
		return resDims;
	}

	@Override
	public final Img< T > compute( final IterableInterval< T >[] intervals, final Img< T > res )
	{

		if ( m_invalidDims == null )
			initConstants( intervals );

		RandomAccess< T > randomAccess = res.randomAccess();
		Arrays.sort( intervals, new IntervalComperator() );

		long[] offset = new long[ intervals[ 0 ].numDimensions() ];
		long[] intervalWidth = new long[ intervals[ 0 ].numDimensions() ];

		intervals[ 0 ].min( offset );
		intervals[ 0 ].dimensions( intervalWidth );

		writeInterval( randomAccess, intervals[ 0 ], offset );

		for ( int i = 1; i < intervals.length; i++ )
		{
			for ( int d = 0; d < intervals[ i ].numDimensions(); d++ )
			{
				if ( intervals[ i ].min( d ) != intervals[ i - 1 ].min( d ) )
				{
					for ( int innerD = d + 1; innerD < intervals[ i ].numDimensions(); innerD++ )
					{
						intervalWidth[ innerD ] = 0;
					}

					offset[ d ] = intervals[ i ].min( d ) - intervalWidth[ d ];
					intervalWidth[ d ] += intervals[ i ].dimension( d );
				}

			}

			writeInterval( randomAccess, intervals[ i ], offset );
		}
		return res;
	}

	/*
	 * Writes an interval into the result to resulting img with respect to the
	 * offset
	 */
	private void writeInterval( RandomAccess< T > resAccess, IterableInterval< T > interval, long[] offset )
	{
		Cursor< T > localizingCursor = interval.localizingCursor();

		while ( localizingCursor.hasNext() )
		{
			localizingCursor.fwd();

			int offsetCtr = 0;
			for ( int d = 0; d < interval.numDimensions(); d++ )
			{
				if ( m_invalidDims.contains( d ) )
				{
					offsetCtr++;
					continue;
				}
				resAccess.setPosition( localizingCursor.getIntPosition( d ) - offset[ d ], d - offsetCtr );
			}
			resAccess.get().set( localizingCursor.get() );

		}
	}

	@Override
	public UnaryOutputOperation< IterableInterval< T >[], Img< T >> copy()
	{
		return new MergeIterableIntervals< T >( m_factory, m_adjustDimensionality );
	}

	@Override
	public Img< T > compute( IterableInterval< T >[] in )
	{
		return compute( in, createEmptyOutput( in ) );
	}

	public List< Integer > getInvalidDims()
	{
		return m_invalidDims;
	}
}