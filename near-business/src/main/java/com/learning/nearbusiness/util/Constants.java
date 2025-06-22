package com.learning.nearbusiness.util;

import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public class Constants {

    private Constants() {}

    public static final class Index {
        public static final IndexCoordinates SUGGESTIONS =
                IndexCoordinates.of("suggestions");

        public static final IndexCoordinates BUSINESS =
                IndexCoordinates.of("businesses");
    }

    public static final class Suggestion {
        public static final String SEARCH_TERM = "search_term";
        public static final String SUGGEST_NAME = "search-term-suggest";
    }

    public static final class Fuzzy {
        public static final String LEVEL = "1";
        public static final int PREFIX_LENGTH = 2;
    }

    public static final class Business {
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String DESCRIPTION = "description";
        public static final String STATE = "state";
        public static final String LOCATION = "location";
        public static final String CATEGORY = "category";
        public static final String CATEGORY_RAW = "category.raw";
        public static final String OFFERINGS = "offerings";
        public static final String OFFERINGS_RAW = "offerings.raw";
        public static final String RATING = "avg_rating";
        public static final String OFFERINGS_AGGREGATE_NAME = "offerings-term-aggregate";
    }
}
