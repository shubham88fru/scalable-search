package com.learning.nearbusiness.util;

import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;

public class ElasticSearchUtil {

    public static Suggester buildCompletionSuggester(String suggestName, String field,
                                                     String prefix, int limit) {

        SuggestFuzziness fuzziness = SuggestFuzziness.of(builder -> builder
                .fuzziness(Constants.Fuzzy.LEVEL)
                .prefixLength(Constants.Fuzzy.PREFIX_LENGTH));

        CompletionSuggester completionSuggester =
                CompletionSuggester.of(builder -> builder.field(field)
                .size(limit).fuzzy(fuzziness)
                .skipDuplicates(true));

        FieldSuggester fieldSuggester = FieldSuggester
                .of(builder -> builder.prefix(prefix).completion(completionSuggester));

        return Suggester.of(builder -> builder.suggesters(suggestName, fieldSuggester));
    }
}
