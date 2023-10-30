package space.tscg;

import space.tscg.internal.local.LocalTestingData;

public class Test
{
    public static void main(String[] args)
    {
        //System.out.println(LocalTestingData.fleetCarrierDataAsString());
        var carr = LocalTestingData.testingFleetCarrierData();
        
        System.out.println(carr.getCallsign());
        System.out.println(carr.getName());
    }
}
