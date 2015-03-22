/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.common.util;

/**
 *
 * @author Fer
 */
public class RestCommonPaths {

    /**
     * API Version
     */
    public static final String API_VERSION = "v1/";
    /**
     * <code>/v1/authentication</code>
     */
    public static final String AUTHENTICATION = API_VERSION + "authentication";
    public static final String IMAGE = "image";
    // Nouns
    private static final String SLASH = "/";
    private static final String USERS = "users";
    private static final String BIKES = "bikes";
    private static final String GROUPS = "groups";
    private static final String EVENTS = "events";
    private static final String PRACTICES = "practices";
    private static final String ROUTES = "routes";
    private static final String SEGMENTS = "segments";
    private static final String MEMBERS = "members";
    private static final String MESSAGES = "messages";
    private static final String REQUEST = "requests";
    private static final String NOTIFICATIONS = "notifications";
    private static final String FRIENDS = "friends";
    private static final String THUMBNAIL = "thumbnail";
    private static final String GPX = ".gpx";
    private static final String RESULTS = "results";
    /**
     * query
     */
    public static final String QUERY = "query";
    public static final String BBOX = "bbox";
    public static final String DATE = "date";
    public static final String FROM_DATE = "from";
    public static final String TO_DATE = "to";
    public static final String QUERY_STRING_MAP = "?" + QUERY + "={" + QUERY + "}";
    public static final String QUERY_BOUNDS_MAP = "?" + BBOX + "={" + BBOX + "}";
    public static final String QUERY_DATE_RANGE_MAP = "?" + FROM_DATE + "={" + FROM_DATE + "}&" + TO_DATE + "={" + TO_DATE + "}";
    public static final String QUERY_DATE_MAP = "?" + DATE + "={" + DATE + "}";
    /**
     * search?q={query}
     */
    public static final String SEARCH = "search";
    //    IDs
    public static final String USER_ID = "userId";
    public static final String FRIEND_ID = "friendId";
    public static final String BIKE_ID = "bikeId";
    public static final String GROUP_ID = "groupId";
    public static final String EVENT_ID = "eventId";
    public static final String PRACTICE_ID = "practiceId";
    public static final String ROUTE_ID = "routeId";
    public static final String SEGMENT_ID = "segmentId";
    public static final String MULTIMEDIA_ID = "multimediaId";
    public static final String MESSAGE_ID = "messageId";
    public static final String REQUEST_ID = "requestId";
    //
    // REST Path querys for USERS
    //
    /**
     * Path for add users by id
     * <code>/v1/users</code>
     */
    public static final String USER = API_VERSION + USERS;
    /**
     * <code>/v1/users/{userId}</code>
     */
    public static final String USER_BY_ID = USER + SLASH + "{" + USER_ID + "}";
    /**
     * <code>/v1/users/{userId}/image</code>
     */
    public static final String USER_IMAGE_BY_ID = USER_BY_ID + SLASH + IMAGE;
    /**
     * <code>/v1/users/{userId}/thumbnail</code>
     */
    public static final String USER_THUMBNAIL_BY_ID = USER_BY_ID + SLASH + THUMBNAIL;
    /**
     * <code>/v1/users/{userId}/friends</code>
     */
    public static final String USER_FRIENDS = USER_BY_ID + SLASH + FRIENDS;
    /**
     * <code>/v1/users/{userId}/friends/{friendId}</code>
     */
    public static final String USER_FRIENDS_BY_ID = USER_BY_ID + SLASH + FRIENDS + SLASH + "{" + FRIEND_ID + "}";
    /**
     * <code>/v1/users/{userId}/friends/requests</code>
     */
    public static final String USER_FRIENDS_REQUEST = USER_BY_ID + SLASH + FRIENDS + SLASH + REQUEST;
    /**
     * <code>/v1/users/{userId}/friends/requests/{requestId}</code>
     */
    public static final String USER_FRIENDS_REQUEST_BY_ID = USER_BY_ID + SLASH + FRIENDS + SLASH + REQUEST + SLASH + "{" + REQUEST_ID + "}";
    /**
     * <code>/v1/users/{userId}/notifications</code>
     */
    public static final String USER_NOTIFICATION = USER_BY_ID + SLASH + NOTIFICATIONS;
    /**
     * <code>/v1/users/{userId}/bikes</code>
     */
    public static final String USER_BIKES = USER_BY_ID + SLASH + BIKES;
    /**
     * <code>/v1/users/{userId}/groups/</code>
     */
    public static final String USER_GROUPS = USER_BY_ID + SLASH + GROUPS;
    /**
     * <code>/v1/users/{userId}/events</code>
     */
    public static final String USER_EVENTS = USER_BY_ID + EVENTS;
    /**
     * <code>/v1/users/{userId}/practices</code>
     */
    public static final String USER_PRACTICES = USER_BY_ID + PRACTICES;
    /**
     * <code>/v1/users/{userId}/bikes/{bikeId}</code>
     */
    public static final String BIKE_BY_ID = USER_BY_ID + SLASH + BIKES + SLASH + "{" + BIKE_ID + "}";
    /**
     * <code>/v1/users/{userId}/bikes/{bikeId}/image</code>
     */
    public static final String BIKE_IMAGE_BY_ID = USER_BY_ID + SLASH + BIKES + SLASH + "{" + BIKE_ID + "}" + SLASH + IMAGE;
    /**
     * <code>/v1/users/{userId}/bikes/{bikeId}/thumbnail</code>
     */
    public static final String BIKE_THUMBNAIL_BY_ID = USER_BY_ID + SLASH + BIKES + SLASH + "{" + BIKE_ID + "}" + SLASH + THUMBNAIL;
    //
    // GROUPS
    //
    /**
     * <code>/v1/groups</code>
     */
    public static final String GROUP = API_VERSION + GROUPS;
    /**
     * <code>/v1/groups/{groupId}</code>
     */
    public static final String GROUP_BY_ID = GROUP + SLASH + "{" + GROUP_ID + "}";
    /**
     * <code>/v1/groups/{groupId}/image</code>
     */
    public static final String GROUP_IMAGE_BY_ID = GROUP_BY_ID + SLASH + IMAGE;
    /**
     * <code>/v1/groups/{groupId}/thumbnail</code>
     */
    public static final String GROUP_THUMBNAIL_BY_ID = GROUP_BY_ID + SLASH + THUMBNAIL;
    /**
     * <code>/v1/groups/{groupId}/members</code>
     */
    public static final String GROUP_MEMBERS = GROUP_BY_ID + SLASH + MEMBERS;
    /**
     * <code>/v1/groups/{groupId}/members/{userId}</code>
     */
    public static final String GROUP_MEMBERS_BY_ID = GROUP_BY_ID + SLASH + MEMBERS + "{" + USER_ID + "}";
    /**
     * <code>/v1/groups/{groupId}/messages</code>
     */
    public static final String GROUP_MESSAGES = GROUP_BY_ID + SLASH + MESSAGES;
    /**
     * <code>/v1/groups/{groupId}/messages/{messageId}</code>
     */
    public static final String GROUP_MESSAGES_BY_ID = GROUP_BY_ID + SLASH + MESSAGES + "{" + MESSAGE_ID + "}";
    /**
     * <code>/v1/groups/{groupId}/events</code>
     */
    public static final String GROUP_EVENTS_BY_ID = GROUP_BY_ID + SLASH + EVENTS;
    /**
     * <code>/v1/groups/{groupId}/routes</code>
     */
    public static final String GROUP_ROUTES_BY_ID = GROUP_BY_ID + SLASH + ROUTES;
    //
    //EVENTS
    //
    /**
     * <code>/v1/challengues</code>
     */
    public static final String EVENT = API_VERSION + EVENTS;
    /**
     * <code>/v1/events/{eventId}</code>
     */
    public static final String EVENT_BY_ID = EVENT + SLASH + "{" + EVENT_ID + "}";
    /**
     * <code>/v1/events/{eventId}/members</code>
     */
    public static final String EVENT_MEMBERS = EVENT_BY_ID + SLASH + MEMBERS;
    /**
     * <code>/v1/events/{eventId}/members/{userId}</code>
     */
    public static final String EVENT_MEMBERS_BY_ID = EVENT_BY_ID + SLASH + MEMBERS + SLASH + "{" + USER_ID + "}";
    /**
     * <code>/v1/events/{eventId}/image</code>
     */
    public static final String EVENT_IMAGE_BY_ID = EVENT_BY_ID + SLASH + IMAGE;
    /**
     * <code>/v1/events/{eventId}/thumbnail</code>
     */
    public static final String EVENT_THUMBNAIL_BY_ID = EVENT_BY_ID + SLASH + THUMBNAIL;
    /**
     * <code>/v1/events/{eventId}/messages</code>
     */
    public static final String EVENT_MESSAGES = EVENT_BY_ID + SLASH + MESSAGES;
    /**
     * <code>/v1/events/{eventId}/messages/{messageId}</code>
     */
    public static final String EVENT_MESSAGES_BY_ID = EVENT_BY_ID + SLASH + MESSAGES + SLASH + "{" + MESSAGE_ID + "}";
    /**
     * <code>/v1/events/{eventId}/routes</code>
     */
    public static final String EVENT_ROUTE = EVENT_BY_ID + SLASH + ROUTES;
    /**
     * <code>/v1/events/{eventId}/segments</code>
     */
    public static final String EVENT_SEGMENT = EVENT_BY_ID + SLASH + SEGMENTS;
    /**
     * <code>/v1/events/{eventId}/routes/practices</code>
     */
    public static final String EVENT_ROUTE_PRACTICES = EVENT_ROUTE + SLASH + PRACTICES;
    /**
     * <code>/v1/events/{eventId}/segments/practices</code>
     */
    public static final String EVENT_SEGMENTS_PRACTICES = EVENT_SEGMENT + SLASH + PRACTICES;
    //
    // PRACTICES
    //
    /**
     * <code>/v1/users/{userId}/practices</code>
     */
    public static final String PRACTICE = USER_BY_ID + SLASH + PRACTICES;
    /**
     * <code>/v1/users/{userId}/practices/{practiceId}</code>
     */
    public static final String PRACTICE_BY_ID = PRACTICE + SLASH + "{" + PRACTICE_ID + "}";
    /**
     * <code>/v1/users/{userId}/practices/{practiceId}.gpx</code>
     */
    public static final String PRACTICE_GPX = PRACTICE_BY_ID + GPX;
    /**
     * <code>/v1/users/{userId}/practices/{practiceId}/routes</code>
     */
    public static final String PRACTICE_ROUTE_BY_ID = PRACTICE_BY_ID + SLASH + ROUTES;
    /**
     * <code>/v1/users/{userId}/practices/{practiceId}/segments</code>
     */
    public static final String PRACTICE_SEGMENT_BY_ID = PRACTICE_BY_ID + SLASH + SEGMENTS;
    //
    // ROUTES
    //
    /**
     * <code>/v1/routes</code>
     */
    public static final String ROUTE = API_VERSION + ROUTES;
    /**
     * <code>/v1/routes/last</code>
     */
    public static final String ROUTE_LAST = API_VERSION + ROUTES + SLASH + "last";
    /**
     * <code>/v1/routes/{routeId}</code>
     */
    public static final String ROUTE_BY_ID = ROUTE + SLASH + "{" + ROUTE_ID + "}";
    /**
     * <code>/v1/routes/{routeId}.gpx</code>
     */
    public static final String ROUTE_GPX = ROUTE_BY_ID + GPX;
    /**
     * <code>/v1/routes/{routeId}/segments</code>
     */
    public static final String ROUTE_SEGMENTS = ROUTE_BY_ID + SEGMENTS;
    //
    // SEGMENTS
    //
    /**
     * <code>/v1/segments</code>
     */
    public static final String SEGMENT = API_VERSION + SEGMENTS;
    /**
     * <code>/v1/segments</code>
     */
    public static final String SEGMENT_LAST = API_VERSION + SEGMENTS + SLASH + "last";
    /**
     * <code>/v1/segments/{segmentId}</code>
     */
    public static final String SEGMENT_BY_ID = API_VERSION + SEGMENTS + SLASH + "{" + SEGMENT_ID + "}";
    /**
     * <code>/v1/segments/{segmentId}/results</code>
     */
    public static final String SEGMENT_RESULTS = SEGMENT_BY_ID + SLASH + RESULTS;
    /**
     * <code>/v1/segments/{segmentId}.gpx</code>
     */
    public static final String SEGMENT_GPX = SEGMENT_BY_ID + ".gpx";
    //
    // SEARCHS
    //
    /**
     * <code>/v1/search/users</code>
     */
    public static final String SEARCH_USER = API_VERSION + SEARCH + SLASH + USERS;
    /**
     * <code>/v1/search/users?query={query}</code>
     */
    public static final String SEARCH_USER_QUERY = SEARCH_USER + QUERY_STRING_MAP;
    /**
     * <code>/v1/search/groups</code>
     */
    public static final String SEARCH_GROUP = API_VERSION + SEARCH + SLASH + GROUPS;
    /**
     * <code>/v1/search/groups?query={query}</code>
     */
    public static final String SEARCH_GROUP_QUERY = SEARCH_GROUP + QUERY_STRING_MAP;
    /**
     * <code>/v1/search/users/{userId}/practices</code>
     */
    public static final String SEARCH_USER_PRACTICE = API_VERSION + SEARCH + SLASH + USERS + SLASH + "{" + USER_ID + "}" + SLASH + PRACTICES;
    /**
     * <code>/v1/search/users/{userId}/practices?f={from}&t={to}</code>
     */
    public static final String SEARCH_USER_PRACTICES_BY_DATE_RANGE = SEARCH_USER_PRACTICE + QUERY_DATE_RANGE_MAP;
    /**
     * <code>/v1/search/users/{userId}/practices?d={date}</code>
     */
    public static final String SEARCH_USER_PRACTICES_BY_DATE = SEARCH_USER_PRACTICE + QUERY_DATE_MAP;
    /**
     * <code>/v1/search/routes</code>
     */
    public static final String SEARCH_ROUTE = API_VERSION + SEARCH + SLASH + ROUTES;
    /**
     * <code>/v1/search/routes?bounds={bounds}</code>
     */
    public static final String SEARCH_ROUTE_BY_GEOM = SEARCH_ROUTE + QUERY_BOUNDS_MAP;
    /**
     * <code>/v1/search/routes?query={query}</code>
     */
    public static final String SEARCH_ROUTE_BY_NAME = SEARCH_ROUTE + QUERY_STRING_MAP;
    /**
     * <code>/v1/search/segments</code>
     */
    public static final String SEARCH_SEGMENTS = API_VERSION + SEARCH + SLASH + SEGMENTS;
    /**
     * <code>/v1/search/routes?bounds={bounds}</code>
     */
    public static final String SEARCH_SEGMENTS_BY_GEOM = SEARCH_SEGMENTS + QUERY_BOUNDS_MAP;
    /**
     * <code>/v1/search/events</code>
     */
    public static final String SEARCH_EVENT = API_VERSION + SEARCH + SLASH + EVENTS;
    /**
     * <code>/v1/search/events?query={query}</code>
     */
    public static final String SEARCH_EVENT_BY_NAME = SEARCH_EVENT + QUERY_STRING_MAP;
    /**
     * <code>/v1/search/events?f={from}&t={to}</code>
     */
    public static final String SEARCH_EVENTS_BY_DATE_RANGE = SEARCH_EVENT + QUERY_DATE_RANGE_MAP;
}
