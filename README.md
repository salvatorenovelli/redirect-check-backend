
# Redirect Check Backend

A cloud friendly single page application to help SEO verify redirect specs have been implemented correctly and don't have regression over time.

It takes an excel spreadsheet as input. In the spreadsheet you can specify a list of urls and the expected redirect destination, and the application will check that for you outputting an excel report.

##Usage

- Deploy the application to any cloud provider. ( Tested on Heroku)
- Drag & drop your XLS/XLSX file on to the home page to start the analysis
- Wait for the process to finish (progress bar at 100%)
- Download the output file


###How to create a valid input file to run the analysis:

- The input should be formatted with two columns: `sourceURI`,`expectedDestinationURI`. 
- No titles or headers are necessary.


####Example (Excel):
An excel workbook where the *first* visible sheet has a list of rows where the first two columns contain `sourceURI` and `expectedDestinationURI`). i.e: 

      |                     A                   |                B                |
    1 | http://example.com                      | http://www.example.com/         |
    2 | http://www.example.com/nonexistentpage  | http://www.example.com/notfound |
    ...etc...

Optionally you can add a third column with the expected status code (in case you expect something other than 200). Where not specified, we'll expect 200

      |                     A                   |                B                |   C   |
    1 | http://example.com                      | http://www.example.com/         |       |
    2 | http://www.example.com/nonexistentpage  | http://www.example.com/notfound |  404  |
    ...etc...

###Output
An xlsx file with the actual redirect destination, HTTP status code, result (as is SUCCESS or FAILURE) result of every redirect in the input.
   


##Conceps
In [SEO][2], during a website structure/domain migration is common to have a very long list of URLs that need to be redirected to another location, and this list needs to be checked periodically for completion and regression.

Creating such list is already cumbersome but verifying it (periodically) is repetitive, therefore should (must!) be automated. 

In this project I'll use Spring Cloud and [Spring Cloud Stream][3] concepts, and once working, I'll migrate it to [Reactive Streams][4]


  [1]: https://github.com/salvatorenovelli/redirect-check-cl/releases
  [2]: https://en.wikipedia.org/wiki/Search_engine_optimization
  [3]: https://cloud.spring.io/spring-cloud-stream/
  [4]: https://spring.io/blog/2016/02/09/reactive-spring
