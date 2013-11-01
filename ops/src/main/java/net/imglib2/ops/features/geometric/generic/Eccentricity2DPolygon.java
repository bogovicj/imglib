package net.imglib2.ops.features.geometric.generic;

import java.awt.geom.Rectangle2D;

import net.imglib2.ops.features.AbstractFeature;
import net.imglib2.ops.features.annotations.RequiredFeature;
import net.imglib2.ops.features.providers.GetPolygonFromBitmask;
import net.imglib2.type.numeric.real.DoubleType;

public class Eccentricity2DPolygon extends AbstractFeature< DoubleType >
{
	@RequiredFeature
	GetPolygonFromBitmask polygon;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name()
	{
		return "Eccentricity on Polygon Feature";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Eccentricity2DPolygon copy()
	{
		return new Eccentricity2DPolygon();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DoubleType recompute()
	{
		Rectangle2D rec = polygon.get().getBounds2D();

		double result = ( rec.getWidth() > rec.getHeight() ) ? rec.getWidth() / rec.getHeight() : rec.getHeight() / rec.getWidth();

		return new DoubleType( result );
	}

}