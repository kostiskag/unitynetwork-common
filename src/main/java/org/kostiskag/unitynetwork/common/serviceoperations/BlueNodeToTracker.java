package org.kostiskag.unitynetwork.common.serviceoperations;

public enum BlueNodeToTracker {

    GREET("BLUENODE"),
    OFFERPUB("OFFERPUB"),
    BN_EXITS_ON_PUBLIC_KEY_NOT_SET("EXIT"),

    LEASE("LEASE"),
        LEASE_SUCCESS_RESPONSE("LEASED"),
        LEASE_FAIL_RESPONSE("LEASE_FAILED"),
        LEASE_ERROR_RESPONSE("SYSTEM_ERROR"),

    RELEASE("RELEASE"),
        RELEASE_SUCCESS_RESPONSE("RELEASED"),
        RELEASE_FAIL_RESPONSE("RELEASE_FAILED"),

    LEASE_RN("LEASE_RN"),
        LEASE_RN_SUCCESS_RESPONSE("LEASED"),
        LEASE_RN_FAIL_RESPONSE("AUTH_FAILED"),
        LEASE_RN_FAIL_RESPONSE_ALREADY_LEASED("ALLREADY_LEASED"),

    RELEASE_RN("RELEASE_RN"),
        RELEASE_RN_SUCCESS_RESPONSE("RELEASED"),
        RELEASE_RN_FAIL_RESPONSE("NOT_AUTHORIZED"),
        RELEASE_RN_ERROR_RESPONSE("SYSTEM_ERROR"),

    GETPH("GETPH"),
        GETPH_FAIL_RESPONSE("NOT_FOUND"),

    CHECK_RN("CHECK_RN"),
        CHECK_RN_FAIL_RESPONSE("OFFLINE"),

    CHECK_RNA("CHECK_RNA"),
        CHECK_RNA_FAIL_RESPONSE("OFFLINE"),

    GETBNPUB("GETBNPUB"),
        GETBNPUB_FAIL_RESPONSE("NONE"),

    GETRNPUB("GETRNPUB"),
        GETRNPUB_FAIL_RESPONSE("NONE"),

    LOOKUP_H("LOOKUP_H"),
        LOOKUP_H_FAIL_RESPONSE("NOT_FOUND"),

    LOOKUP_V("LOOKUP_V"),
        LOOKUP_V_FAIL_RESPONSE("NOT_FOUND"),

    REVOKEPUB("REVOKEPUB"),

    TRACKER_RESPONCE_TO_AUTHENTICATED_WRONG_OPTION("WRONG_COMMAND"),
    TRACKER_RESPONCE_TO_ALREADY_AUTHENTICATED_TRYING_TO_REAUTH("WRONG_COMMAND");

    private String val;

    BlueNodeToTracker() {
        //when you call the default constructor, it means that the value is the same as the enum's name
        this.val = this.toString();
    }

    BlueNodeToTracker(String value) {
        this.val = value;
    }

    //always use this.value() instead of this.toString()!!!!
    public String value() {
        return val;
    }
}
