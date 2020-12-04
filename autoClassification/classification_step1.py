#!/usr/bin/env python
# coding: utf-8

import numpy as np
import pandas as pd
import re
import time
import glob2
import shutil
import sys
import mysql.connector

dbconn = mysql.connector.connect(host="localhost", port="3307", user="client", passwd="client", database="data_classification")

def merge(query, values, bufferd=True):
    global dbconn
    try:
        cursor = dbconn.cursor(buffered=bufferd)
        cursor.execute(query, values)
        dbconn.commit()
    except Exception as e:
        dbconn.rollback()
        raise e

server_repo = "C:/Users/Yun_YoungHyun/dananuo"
server_link = "http://localhost:8082//DataClassification/files"

productmodeltable_drop_dupl = pd.read_csv(server_repo + '/productData.csv', error_bad_lines=False, dtype=str).fillna('없음')

df_product = productmodeltable_drop_dupl[['중분류 카테고리명', '소분류 카테고리명', '세분류 카테고리명']]

df_product = df_product.drop_duplicates(['중분류 카테고리명', '소분류 카테고리명', '세분류 카테고리명'])

product_arr = np.array(df_product)

file_list = glob2.glob(server_repo + '/openmarket_goods/*.csv')

if(len(file_list) == 0):
    print("*****There are no more files.*****")
    sys.exit()

openmarket_file = file_list[0]

openmarket_file_name = openmarket_file.split('\\')[1]

df_openmarket = pd.read_csv(openmarket_file, error_bad_lines=False, dtype=str).fillna('없음')

print("openmarket_file => " + str(openmarket_file))
print("classfication start now!")

try:
    merge("update csv_files set file_status='IN PROGRESS' where file_name=%s", (openmarket_file_name,))
except Exception as e:
    print(e)

linktable = []
for pdcategory in product_arr:
    omTable = df_openmarket[(df_openmarket['중분류 카테고리명'] == pdcategory[0]) & (df_openmarket['소분류 카테고리명'] == pdcategory[1]) & (df_openmarket['세분류 카테고리명'] == pdcategory[2])]
    omTabletoList = np.array(omTable['상품명'])
    pdTable = productmodeltable_drop_dupl[(productmodeltable_drop_dupl['중분류 카테고리명'] == pdcategory[0]) & (productmodeltable_drop_dupl['소분류 카테고리명'] == pdcategory[1]) & (productmodeltable_drop_dupl['세분류 카테고리명'] == pdcategory[2])]
    omlistseries = pd.Series(omTabletoList)
    for i in range(len(pdTable)):
        containsRes = omlistseries.str.contains(pdTable.iloc[i, 1], case=False)
        for j in range(len(containsRes)):
            if containsRes[j] == True:
                linktable.append([pdTable.iloc[i, 0], omTable.iloc[j, 0], omTable.iloc[j, 1]])
                df_openmarket.drop(df_openmarket.loc[(df_openmarket['제휴사 코드'] == omTable.iloc[j, 0]) & (df_openmarket['상품 코드'] == omTable.iloc[j, 1])].index, inplace=True)
# print(time.time() - start)

print("classfication job done!")

df_linktable = pd.DataFrame(linktable, columns=['다나와 코드', '마켓 코드', '마켓 상품 코드'])

try:
    nowtime = str(time.time())
    noneClassified_file = "noneClassified" + nowtime + ".csv"
    df_openmarket.to_csv(server_repo + "/none_classified/" + noneClassified_file, index=False)
    print("noneClassified Data is converted to csv")
    noneClassified_uri = server_link + "/none_classified/" + noneClassified_file
    merge("insert into noneclassified_files (file_name, file_uri, file_date) values (%s, %s, %s)", (noneClassified_file, noneClassified_uri, nowtime))
    
    classified_file = "Classified" + nowtime + ".csv"
    df_linktable.to_csv(server_repo + "/classified/" + classified_file, index=False)
    print("Classified Data is converted to csv")
    classified_uri = server_link + "/classified/" + classified_file
    merge("insert into classified_files (file_name, file_uri, file_date) values (%s, %s, %s)", (classified_file, classified_uri, nowtime))
    
    shutil.move(openmarket_file, server_repo + "/processed/" + openmarket_file_name)
    print("processed openmarket data is moved to processed folder")
    openmarket_file_uri = server_link + "/processed/" + openmarket_file_name
    merge("update csv_files set file_uri=%s, file_status=%s where file_name=%s", (openmarket_file_uri, 'DONE', openmarket_file_name))
except Exception as e:
    print(e)
finally:
    dbconn.close()