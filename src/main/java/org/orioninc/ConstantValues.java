package org.orioninc;

public class ConstantValues {
    public final static String API_ENDPOINT_STACKOVERFLOW = "https://api.stackexchange.com";
    public final static int NUMBER_OF_QUESTIONS_FROM_STACKOVERFLOW = 10;
    public final static String BEARER_TOKEN_HASTEBIN = "Bearer " + System.getenv("TOKEN");
    public final static String API_ENDPOINT_HASTEBIN = "https://hastebin.com";
    public final static String POST_ROUTE_HASTEBIN = "/documents";
    public final static String API_ENDPOINT_STORAGESERVICE = System.getenv("API_ENDPOINT_STORAGESERVICE");
    public final static String POST_ROUTE_STORAGESERVICE = "/api/document";
    public final static String HEADER_SOURCE = "ServiceAPI";

}
