/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

/**
 * @author amit bhayani
 * 
 */
public class ProvideSubscriberLocationRequestIndicationTest {

	MapServiceFactory mapServiceFactory = new MapServiceFactoryImpl();

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
	public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
		// The trace is from Brazilian operator
		byte[] rawData = new byte[] { 0x30, 0x41, 0x30, 0x03, (byte) 0x80, 0x01, 0x00, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa0, 0x1b,
				(byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c,
				(byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, (byte) 0x82, 0x08, 0x27, (byte) 0x94,
				(byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7, (byte) 0x86, 0x01, 0x01, (byte) 0xa7, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00, (byte) 0x89,
				0x02, 0x01, (byte) 0xfe };

		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();

		ProvideSubscriberLocationRequestIndicationImpl reqInd = new ProvideSubscriberLocationRequestIndicationImpl();
		reqInd.decodeAll(asn);

		LocationType locationType = reqInd.getLocationType();
		assertNotNull(locationType);
		assertEquals(LocationEstimateType.currentLocation, locationType.getLocationEstimateType());

		ISDNAddressString mlcNumber = reqInd.getMlcNumber();
		assertNotNull(mlcNumber);
		assertEquals(AddressNature.international_number, mlcNumber.getAddressNature());
		assertEquals(NumberingPlan.ISDN, mlcNumber.getNumberingPlan());
		assertEquals("55619007", mlcNumber.getAddress());

		LCSClientID lcsClientId = reqInd.getLCSClientID();
		assertNotNull(lcsClientId);
		assertEquals(LCSClientType.plmnOperatorServices, lcsClientId.getLCSClientType());
		assertEquals(LCSClientInternalID.broadcastService, lcsClientId.getLCSClientInternalID());
		LCSClientName lcsClientName = lcsClientId.getLCSClientName();
		assertNotNull(lcsClientName);
		assertEquals((byte) 0x0f, lcsClientName.getDataCodingScheme());
		assertEquals("ndmgapp2ndmgapp2", lcsClientName.getNameString().getString());

		IMSI imsi = reqInd.getIMSI();
		assertNotNull(imsi);
		assertEquals(724l, imsi.getMCC());
		assertEquals(99l, imsi.getMNC());
		assertEquals("9900000007", imsi.getMSIN());

		assertEquals(1, reqInd.getLCSPriority());

		LCSQoS lcsQoS = reqInd.getLCSQoS();
		assertNotNull(lcsQoS);
		ResponseTime respTime = lcsQoS.getResponseTime();
		assertNotNull(respTime);
		assertEquals(ResponseTimeCategory.lowdelay, respTime.getResponseTimeCategory());

		SupportedGADShapes suppGadShapes = reqInd.getSupportedGADShapes();
		assertNotNull(suppGadShapes);
		assertTrue(suppGadShapes.getEllipsoidArc());
		assertTrue(suppGadShapes.getEllipsoidPoint());
		assertTrue(suppGadShapes.getEllipsoidPointWithAltitude());
		assertTrue(suppGadShapes.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid());

		assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyCircle());
		assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyEllipse());
		assertTrue(suppGadShapes.getPolygon());
	}

	@Test
	public void testEncode() throws Exception {
		// The trace is from Brazilian operator
		byte[] data = new byte[] { 0x30, 0x41, 0x30, 0x03, (byte) 0x80, 0x01, 0x00, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa0, 0x1b,
				(byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c,
				(byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, (byte) 0x82, 0x08, 0x27, (byte) 0x94,
				(byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7, (byte) 0x86, 0x01, 0x01, (byte) 0xa7, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00, (byte) 0x89,
				0x02, 0x01, (byte) 0xfe };

		LocationType locationType = new LocationTypeImpl(LocationEstimateType.currentLocation, null);
		ISDNAddressString mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "55619007");

		USSDString nameString = mapServiceFactory.createUSSDString("ndmgapp2ndmgapp2");
		LCSClientName lcsClientName = new LCSClientNameImpl((byte) 0x0f, nameString, null);

		LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null, LCSClientInternalID.broadcastService, lcsClientName, null,
				null, null);

		IMSI imsi = mapServiceFactory.createIMSI(724l, 99l, "9900000007");

		LCSQoS lcsQoS = new LCSQoSImpl(null, null, null, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null);

		SupportedGADShapes supportedGADShapes = new SupportedGADShapesImpl(true, true, true, true, true, true, true);

		ProvideSubscriberLocationRequestIndicationImpl reqInd = new ProvideSubscriberLocationRequestIndicationImpl(locationType, mlcNumber, lcsClientID, null,
				imsi, null, null, null, 1, lcsQoS, null, supportedGADShapes, null, null, null, null, null, null);

		AsnOutputStream asnOS = new AsnOutputStream();
		reqInd.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data, encodedData));
	}
}
