# Introduction

Our team of developers and analysts were approached by an online store based in the UK called the London Gift Shop (referred to as LGS). LGS was concerned that its revenue had not grown as anticipated within the duration of its online store (10 years) and sought to have its consumer data analysed by Jarvis's tech consultants. The goal was to build a big-picture proof of concept that would allow the marketing team at LGS to develop new strategies for engaging current and future customers, among them major wholesalers that bring tons of business.

# Implementation

The code, data wrangling, transformation, and analysis were all implemented through a Jupyter Notebook file and using Python 3 and Python libraries such as NumPy and Pandas for manipulating Data Frames, Matplotlib for data visualisation, and SQLAlchemy for creating an engine with which to interface with the local PostgreSQL database.

## Project Architecture

For the purposes of this project, the Jarvis team was not allowed access to utilise the LGS Azure environment, but briefly, we will touch upon the architecture of their cloud system. The LGS Online Store consists of a front-end built on Azure Blob, HTML and JavaScript scripts. This connects to an API backend housed on an Azure SQL server that employs an AKS Cluster for microservices.

![LGS Achitecture](/python_data_analytics/python_data_wrangling/data/LGS_Jarvis.png "LGS Architecture")

Transactional data from between 01/12/2009 and 09/12/2011 was transformed to omit the personal information of LGS's customers and then transferred to us for analysis via an SQL file. The data was then housed and shared via a data warehouse built locally on PostgreSQL for ease of access to our team members.

## Data Analytics and Wrangling

You can find the Jupyter notebook for the analysis [here!](retail_analysis.ipynb)

From our analysis regarding monthly earnings and growth in monthly earnings, we concluded that revenue bolsters immensely around the Wintertime holidays, but then dips drastically afterwards each year. Knowing this, we recommended to LGS to potentially run marketing ads for their store after the holidays as well to keep the interest of consumers and help mitigate this drastic dip.

More importantly, through our RFM Segmentation analysis, we were able to determine that a substantial number of LGS's customers were in the 'Hibernating' group - meaning they were customers that had not made any purchases in quite some time. We recommended to LGS that offering promotions to these customers might incentivise them to make new purchases and that LGS should focus on pushing these customers to become 'Champions' i.e. consumers that make up the majority of spenders. LGS was advised to offer promotions to this demographic as well.

Finally, we looked at a batch of customers that had previously made big purchases but have since then not revisited the online store, labelled "Can't lose". LGS was advised to heavily market in favour of this demographic as they had huge spending potential.

## Improvements

- It might benefit us to expand upon the insights we saw through RFM segmentation by graphing the data (for example, using a tree graph to visualise these findings)
- Something we did not address but could be addressed in the future is shipping costs and whether this affects customers' motivation to finalise purchases.
- For wholesalers, it might be beneficial to look at their data and analyse whether the quantity of items purchased might be affected by discounts for products purchased in bulk.
