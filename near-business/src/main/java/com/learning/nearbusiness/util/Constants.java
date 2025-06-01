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
}
