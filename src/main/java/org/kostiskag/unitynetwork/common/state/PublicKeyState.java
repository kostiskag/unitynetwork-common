package org.kostiskag.unitynetwork.common.state;

public enum PublicKeyState {
    NOT_SET,
    KEY_SET,
    KEY_IS_SET,
    WRONG_TICKET,
    KEY_REVOKED,
    SYSTEM_ERROR;

    String value;

    PublicKeyState() {
        value = this.name();
    }

    PublicKeyState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
