package org.kostiskag.unitynetwork.common.calculated;

public enum NumericConstraints {

    // sometimes the user has to provide an input
    // these are the user input max sizes
    MAX_INT_STR(String.valueOf(Integer.MAX_VALUE).length()),
    MAX_STR_LEN_SMALL(128),
    MAX_STR_LEN_LARGE(256),
    MIN_USERNAME(4),
    MIN_PASSWORD(5),
    //Careful the below two may not apply in a case where the
    //user has decided to supply a domain name! use them
    //ONLY in cases where it is sure that the user will supply an address!
    MIN_STR_ADDR("1.1.1.1".length()),
    MAX_STR_ADDR("255.255.255.255".length()),

    MAX_ALLOWED_PORT_NUM(65535),
    /*
        network maths

        10.0.0.0 is our virtual network of hosts which consists of 3 bytes of network part
        if we subtract one for the network address and another one for the broadcast address we
        may get the allowed host number on the network which is

        VIRTUAL_NETWORK_ADDRESS_CAPACITY

        if we want to find the effective number of hosts capacity we have to also subtract the
        SYSTEM_RESERVED_ADDRESS_NUMBER

        VIRTUAL_NETWORK_ADDRESS_EFFECTIVE_CAPACITY
     */
    SYSTEM_RESERVED_ADDRESS_NUMBER(128),
    VIRTUAL_NETWORK_ADDRESS_CAPACITY((int) (Math.pow(2, 24) - 2)), // 10.0.0.0 we have 3 bytes of network - 2 for network and host part
    VIRTUAL_NETWORK_ADDRESS_EFFECTIVE_CAPACITY(VIRTUAL_NETWORK_ADDRESS_CAPACITY.size() - SYSTEM_RESERVED_ADDRESS_NUMBER.size());

    private int len;

    NumericConstraints(int size) {
        this.len = size;
    }

    public int size() {
        return len;
    }

}
