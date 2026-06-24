package com.steamlibrary.exception;

public class SteamAccountNotFoundException extends RuntimeException {

    public SteamAccountNotFoundException(String message) {
        super(message);
    }
}
