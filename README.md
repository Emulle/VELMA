# VELMA
### Installation Instructions
Maven is used to create a runnable JAR with the command: ```mvn clean install```  
The JAR can be run with the following command: ```java -jar VELMA1-1.0-SNAPSHOT.jar```
### Vocational Education and Labour Market Analyser
This program fetches HVE curriculums from Susanav API and MYH API, together with job ads from JobStream API and conduct topic modelling on the data. VELMA was created for use in the thesis by Emil Bäckstrand and Rasmus Djupedal, and its purpose is to aid in identifying the needs of the software industry compared to what is taught in higher vocational education.  
   
To run the program you will have to download the curriculums by clicking on the **Search Curriculum** button, and collect job ads by clicking on the **Search Job Ads** button. By clicking on the **Settings** button, it is possible to choose to only download job ads written in English or Swedish, and choose among seven different sub-occupations within software development and programming.  
  
With the HVE curriculums downloaded and job ads collected the topic modelling can be started by clicking on the **Analyse** button. However, in the **Settings** window there are several settings regarding the modelling process. The settings covered are: 
* *Alpha*: Covers the likehood of topics in each document, higher value tells the algorithm that each document is liekly to contain a larger number of topics.  
* *Beta*: Covers the likehood of words in each topic, where a high value tells the algorithm that each topic is likely to contain many words.  
* *No. of topics*: How many topics the algorithm should look for.  
* *No. of threads*: How many threads to do work on.  
* *No. of iterations*: How many iterations to run the algorithm, recommended value: 1000 - 2000.  
  
During the analysis a progress window will appear, and when the process is complete a window will appear with the found topics and included words.  
  
Future work of the tool includes implementing a keyword analyser which will produce statistical results.
