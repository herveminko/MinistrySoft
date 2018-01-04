SET CurrentDate=%date:~-4,4%%date:~-7,2%%date:~-10,2%
SET CurrentTIme=%time:~-11,2%%time:~-8,2%%time:~-5,2%

REM set datetimestamp=%CurrentDate%_%CurrentTIme%
set datetimestamp=%CurrentDate%
echo %datetimestamp%

ant -f build_custom.xml -l build_custom_%datetimestamp%.log