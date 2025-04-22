# Spark Project

## Table of Contents

- [Introduction](#introduction)
- [Summary of Analytics](#summary-of-key-analytical-insights)
- [Databricks Implementation with PySpark](#databricks-implementation-with-pyspark)
- [Databricks Implementation with Scala](#databricks-implementation-with-scala)
- [Future Improvements](#future-improvements)

## Introduction

Following the delivery of our analysis of the London Gift Shop's retail data, our Data Eng team was assigned more work regarding LGS's data solutions architecture. The project involved moving from an implementation that was designed for a single machine (Python/pandas dataframes) to a solution that can handle much larger datasets.  

The team settled on requisitioning a cluster of machines using the Apache Spark framework, which would allow us to perform data processing of much larger groups of data. The ETL and analysis approach was the same as that taken with the pandas dataframe implementation in the previous project. This allowed us to evaluate methods using the pandas/matplotlib packages vs using Spark within a cluster leveraged through the Azure Databricks platform. We also looked at Scala vs PySpark and whether they served any advantages/disadvantages concerning one another, as Spark can accommodate both.

## Summary of Key Analytical Insights

As we did for the Python Data Analytics project, here we looked at:

- Monthly Sales trends and customer activity
- Distribution of Invoice Totals
- RFM Segmentation to determine key focus groups

## Databricks Implementation with PySpark

[Databricks Notebook](/spark/notebook/retail_data_analytics.ipynb)  

The data consisted of transactions from DEC/2009 until DEC/2011. To allow access to the data, we uploaded a csv file to the databricks filesystem and created a DBFS table using the platform's UI menu. We then loaded the table into a spark dataframe and transformed the data per our requirements (example: various aggregates in order to showcase monthly orders vs monthly cancelled orders).  

The team found it easy to implement the same solutions using PySpark dataframes. Overall, re-architecting the data solution revolved heavily around scaling to a much larger dataset and testing out heavier data loads should be a step moving forward. Attached below is an architecture diagram that outlines how PySpark works under the hood.

![PySpark Execution](/spark/misc/spark.png "How PySpark Works with an Apache Spark Cluster")

## Databricks Implementation with Scala

[Databricks Notebook](/spark/notebook/scala_retail_data_analytics.ipynb)

In the same way we did with PySpark, we implemented reading of, transformation of, analysis and visualisation of the data, this time using Scala code. The team concluded that performance might have been slightly better when using Scala, but that it was less intuitive to grasp for those unfamiliar with the Scala APIs. Note that when interpreting Scala code, Spark avoids the overhead of requiring a bridge such as a PySpark Driver, as Spark is written in Scala.

## Future Improvements

- As we evaluate leveraging Apache Spark for out data processing, we should evaluate performance changes as we scale up. This would mean performing similiar work on much larger datasets.
- It might prove beneficial for us to look at ways to finetune spark executor and driver configurations, something we did not look too closely at. For example: modifying ```spark.driver.memory``` to avoid ```OutofMemory``` errors with larger datasets during results collection.
- Similarly, improving executor core allocation would help improve transformations when scaling up. This can be done through adjusting ```spark.executor.cores``` or through enabling ```spark.dynamicAllocation.enabled```  
