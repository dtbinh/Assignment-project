Select DISTINCT b."ID"
From BOOKS b, REVIEWS r , 
XMLTABLE(
          'for $book in /book return $book'
                PASSING b.Request
                COLUMNS
                "ID" VARCHAR2(50) PATH 'ID',
                "TITLE" VARCHAR2(50) PATH 'TITLE') b,
XMLTABLE(
          'for $review in /review return $review'
                passing r.Request1
                COLUMNS
                "BOOK_TITLE" VARCHAR2(50) PATH 'BOOK_TITLE',
                "REVIEW_DATE" VARCHAR2(50) PATH 'REVIEW_DATE') r
WHERE  b."TITLE"=r."BOOK_TITLE" 
       AND r."REVIEW_DATE" Like '2014%';