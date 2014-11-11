Select r."BOOK_TITLE",AVG(r."RATING") as AVERAGE
from REVIEWS r,
XMLTABLE('for $review in /review return $review'
          passing r.Request1
          COLUMNS
          "BOOK_TITLE" varchar(50) PATH 'BOOK_TITLE',
          "RATING" varchar(50) PATH 'RATING') r
Group by r."BOOK_TITLE"
Order by AVERAGE DESC