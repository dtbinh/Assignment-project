Select r."REVIEWER",r."RATING",b."PUBLISH_DATE",r."REVIEW_DATE"
from REVIEWS r, BOOKS b,
XMLTABLE('for $review in /review return $review'
          passing r.Request1
          COLUMNS
          "REVIEWER" varchar(50) PATH 'REVIEWER',
          "RATING" varchar(50) PATH 'RATING',
          "REVIEW_DATE" Date PATH 'REVIEW_DATE',
          "BOOK_TITLE" varchar(50) PATH 'BOOK_TITLE') r,
XMLTABLE('for $book in /book return $book'
          passing b.Request
          COLUMNS
          "TITLE" varchar(50) PATH 'TITLE',
          "PUBLISH_DATE" Date PATH 'PUBLISH_DATE') b
where b."TITLE"=r."BOOK_TITLE" and r."RATING">3
Order by b."PUBLISH_DATE" ASC,r."REVIEW_DATE" DESC