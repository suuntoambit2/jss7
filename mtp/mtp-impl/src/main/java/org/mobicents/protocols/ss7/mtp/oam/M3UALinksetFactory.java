package org.mobicents.protocols.ss7.mtp.oam;


/**
 * 
 * @author amit bhayani
 *
 */
public class M3UALinksetFactory extends LinksetFactory {

    private static final String NAME = "m3ua";
    
	public M3UALinksetFactory() {
		super();
	}
	
    @Override
    String getName() {
        return NAME ;
    }

    @Override
    Linkset createLinkset(String[] options) {
        return null;
    }

}
