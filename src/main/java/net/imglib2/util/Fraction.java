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

package net.imglib2.util;

/**
 * Implements the concept of fractions
 * 
 * @author Stephan Preibisch
 */
public class Fraction 
{
	long numerator, denominator;
	
	/**
	 * Creates a new fraction with the respective values
	 * 
	 * @param numerator (above fraction bar)
	 * @param denominator (below fraction bar)
	 */
	public Fraction( final long numerator, final long denominator )
	{
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	/**
	 * Instantiate a {@link Fraction} with a value of 1
	 */
	public Fraction() { this( 1, 1 ); }
	
	/**
	 * @return - the numerator (above the fraction bar)
	 */
	public long getNumerator() { return numerator; }
	
	/**
	 * @return - the denominator (below the fraction bar)
	 */
	public long getDenominator() { return denominator; }
	
	/**
	 * @return - an estimate of the ratio in double, i.e. numerator/denominator
	 */
	public double getRatio() { return (double)numerator / (double)denominator; }

	/**
	 * Inverts this fraction by exchanging numerator and denominator
	 */
	public void invert()
	{
		final long tmp = numerator;
		numerator = denominator;
		denominator = tmp;
	}
	
	public void mul( final Fraction fraction )
	{
		this.numerator *= fraction.getNumerator();
		this.denominator *= fraction.getDenominator();
	}

	public void div( final Fraction fraction )
	{
		this.numerator *= fraction.getDenominator();
		this.denominator *= fraction.getNumerator();
	}

	/**
	 * Multiply the value with this fraction. Return the ceiled
	 * value (e.g. 10.2 = 11) if the result is a fraction. 
	 * 
	 * @param value
	 * @return
	 */
	public long mulCeil( final long value ) 
	{ 
		final long tmp = value * numerator;
		
		if ( tmp % denominator != 0 )
			return tmp / denominator + 1;
		else
			return tmp / denominator;
	}
	
	@Override
	public Fraction clone()
	{
		return new Fraction( numerator, denominator );
	}
}