/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */


package net.imglib2.ops.function.real;

import net.imglib2.ops.function.Function;
import net.imglib2.ops.pointset.PointSet;
import net.imglib2.type.numeric.RealType;

// TODO - very similar to RealConvolutionFunction : make one call other?

// TODO - this class suffers from the weakness that one needs to
// correctly build the weights without maybe knowing the order of points in
// the PointSet. We will need helper methods in that build a PointSet
// compatible set of weights from a PointSet and some other hints.


/**
 * 
 * @author Barry DeZonia
 */
public class RealWeightedSumFunction<T extends RealType<T>>
	implements Function<PointSet,T>
{
	private final Function<long[],T> otherFunc;
	private StatCalculator<T> calculator;
	private final double[] weights;
	
	public RealWeightedSumFunction(Function<long[],T> otherFunc, double[] weights)
	{
		this.otherFunc = otherFunc;
		this.calculator = null;
		this.weights = weights;
	}
	
	@Override
	public RealWeightedSumFunction<T> copy() {
		return new RealWeightedSumFunction<T>(otherFunc.copy(), weights.clone());
	}

	@Override
	public void compute(PointSet input, T output) {
		if (calculator == null) calculator = new StatCalculator<T>(otherFunc, input);
		else calculator.reset(otherFunc, input);
		double value = calculator.weightedSum(weights);
		output.setReal(value);
	}

	@Override
	public T createOutput() {
		return otherFunc.createOutput();
	}

}