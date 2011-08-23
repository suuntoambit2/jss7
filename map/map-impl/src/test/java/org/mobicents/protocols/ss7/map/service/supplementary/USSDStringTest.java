/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.service.supplementary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * @author amit bhayani
 *
 */
public class USSDStringTest {
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDecode() throws Exception {
		byte[] data = new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte)0xd4 };
		
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		USSDString ussdStr = new USSDStringImpl();
		ussdStr.decodeAll(asn);
		
		assertEquals("*88#", ussdStr.getString());

	}
	
	//TODO Fix the GSMEncoder. This is failing 
	//@Test
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte)0xd4 };
		
		USSDString ussdStr = new USSDStringImpl("*88#", null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		ussdStr.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue(Arrays.equals(data, encodedData));
	}
}
