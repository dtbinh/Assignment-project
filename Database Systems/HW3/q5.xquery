Select DISTINCT r."REVIEWER",COUNT(r."BOOK_TITLE") as TOTALCOUNT,AVG(r."RATING") as AVERAGE
from REVIEWS r,
XMLTABLE('for $review in /review return $review'
          passing r.Request1
          COLUMNS
          "RATING" varchar(50) PATH 'RATING',
          "REVIEWER" varchar(50) PATH 'REVIEWER',
          "BOOK_TITLE" varchar(50) PATH 'BOOK_TITLE') r
Group by r."REVIEWER"