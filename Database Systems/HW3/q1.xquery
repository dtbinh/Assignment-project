Select DISTINCT r."REVIEWER"
From BOOKS b, REVIEWS r, 
XMLTABLE(
          'for $review in /review return $review'
                passing r.Request1
                COLUMNS
                "REVIEWER" VARCHAR2(50) PATH 'REVIEWER',
                "BOOK_TITLE" VARCHAR2(50) PATH 'BOOK_TITLE') r, 
XMLTABLE(
          'for $book in /book
              where $book/PRICE > 25
                return $book'
                PASSING b.Request
                COLUMNS
                "TITLE" VARCHAR2(50) PATH 'TITLE',
                "PUBLISH_DATE" VARCHAR2(50) PATH 'PUBLISH_DATE') b

WHERE r."BOOK_TITLE" = b."TITLE"
AND  ( b."PUBLISH_DATE" Like '_____08___' OR
       b."PUBLISH_DATE" Like '_____09___' OR
       b."PUBLISH_DATE" Like '_____10___' OR
       b."PUBLISH_DATE" Like '_____11___' OR
       b."PUBLISH_DATE" Like '_____12___' );