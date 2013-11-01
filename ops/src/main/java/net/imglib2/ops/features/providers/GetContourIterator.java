package net.imglib2.ops.features.providers;

import java.awt.Polygon;

import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.ops.features.AbstractFeature;
import net.imglib2.ops.features.Feature;
import net.imglib2.ops.features.PositionIterator;
import net.imglib2.ops.features.annotations.RequiredFeature;

public class GetContourIterator extends AbstractFeature< PositionIterator >
{
	@RequiredFeature
	GetPolygonFromBitmask polygon;

	@Override
	public String name()
	{
		return "Polygon Based Position Iterator";
	}

	@Override
	public Feature< PositionIterator > copy()
	{
		return new GetContourIterator();
	}

	@Override
	protected PositionIterator recompute()
	{

		return new PositionIterator()
		{
			private Polygon poly;

			private Point point;

			private int ctr;

			private int npoints;

			{
				poly = GetContourIterator.this.polygon.get();
				point = new Point( 2 );
				ctr = 0;
				npoints = poly.npoints;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException( "Not supported" );
			}

			@Override
			public Localizable next()
			{
				point.setPosition( poly.xpoints[ ctr ], 0 );
				point.setPosition( poly.ypoints[ ctr ], 1 );

				ctr++;
				return point;
			}

			@Override
			public boolean hasNext()
			{
				return ctr < npoints;
			}

			@Override
			public int numDimensions()
			{
				return 2;
			}
		};
	}
}