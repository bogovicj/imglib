package net.imglib2.ops.features.haralick.features;

import net.imglib2.ops.features.AbstractFeature;
import net.imglib2.ops.features.annotations.RequiredFeature;
import net.imglib2.ops.features.haralick.helpers.CoocStdX;
import net.imglib2.type.numeric.real.DoubleType;

public class Variance extends AbstractFeature< DoubleType >
{

	@RequiredFeature
	private CoocStdX coocStdX = new CoocStdX();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name()
	{
		return "Variance";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance copy()
	{
		return new Variance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DoubleType recompute()
	{
		return new DoubleType( coocStdX.get().get() * coocStdX.get().get() );
	}

}