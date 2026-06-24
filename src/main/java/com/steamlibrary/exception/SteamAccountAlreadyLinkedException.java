package com.steamlibrary.exception;

public class SteamAccountAlreadyLinkedException extends RuntimeException {

    public SteamAccountAlreadyLinkedException(String message) {
        super(message);
    }
}
