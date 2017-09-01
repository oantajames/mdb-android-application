package mdb.com.data;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static mdb.com.data.SortType.FAVORITES;
import static mdb.com.data.SortType.MOST_POPULAR;
import static mdb.com.data.SortType.NOT_DEFINED;
import static mdb.com.data.SortType.TOP_RATED;

@IntDef({TOP_RATED, MOST_POPULAR, FAVORITES, NOT_DEFINED})
@Retention(SOURCE)
public @interface SortType {
    int TOP_RATED = 0;
    int MOST_POPULAR = 1;
    int FAVORITES = 2;
    int NOT_DEFINED = 3;
}
