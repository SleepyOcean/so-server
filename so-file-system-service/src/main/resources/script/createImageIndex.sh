#!/bin/sh

#$1="http://192.168.88.128:9200"
#$2=shards=5
#$3=replicas=1
#$4=operation=create/delete

delIndex()
{
	curl -H "Content-Type: application/json" -XDELETE -i -s "$1/so_image_store_index" > /dev/null
	echo "delete index so_image_store_index successfully !"

}

createIndex()
{
  curl -H "Content-Type: application/json" -XPUT -i -s $1/so_image_store_index -d '
	{
	  "settings": {
		  "index.number_of_shards":'$2',
		  "index.number_of_replicas": '$3'
		},
	  "mappings":
	  {
		"image":{
			"dynamic":"strict",
			"_all":{
				"enabled":false
			},
			"_field_names":{
				"enabled":false
			},
			"properties":{
				"imageId":{
					"type":"keyword"
				},
				"alias":{
					"type":"keyword"
				},
				"path":{
					"type":"keyword"
				},
				"type":{
					"type":"keyword"
				},
				"tags":{
					"type":"keyword"
				},
				"archive":{
					"type":"keyword"
				},
				"associateAttribute":{
					"type":"text"
				},
				"describeInfo":{
					"type":"text"
				},
				"uploadTime":{
					"type":"date",
					"format":"yyyy-MM-dd HH:mm:ss"
				},
				"createTime":{
					"type":"date",
					"format":"yyyy-MM-dd HH:mm:ss"
				},
				"location":{
					"type":"keyword"
				},
				"resolutionRatio":{
					"type":"text"
				},
				"imgSize":{
					"type":"keyword"
				},
				"imgFormat":{
					"type":"keyword"
				}
			}
		}
	  }
	}'
	echo " create so_image_store_index successfully !"

	######################################################################################
	echo -e "\n\n=============================== _aliases ================================"
	curl -H "Content-Type: application/json" -XPOST -i $1/_aliases -d '
	{
		"actions": [
			{
				"add": {
					"index": "so_image_store_index",
					"alias": "so_image_store"
				}
			}
		]
	}' > /dev/null
}

echo $1
echo $2
echo $3
echo $4

if [ "$1" = "" ] ; then
	echo "please input es url, for example: http://172.16.138.40:9200";
	exit;
fi

if [ "$2" = "" ] ; then
	echo "please input shards number, for example: 5";
	exit;
fi

if [ "$3" = "" ] ; then
	echo "please input replicas number, for example: 1";
	exit;
fi

if [ "$4" = "" ] ; then
   echo "no input for operation type; auto run start!!";
   delIndex $1;
   createIndex $1 $2 $3;
elif [  "$4" = "create" ] ; then
   echo "create index start!!"
   createIndex $1 $2 $3;
elif [  "$4" = "delete" ] ; then
   echo "create index start!!"
   delIndex $1;
else
   echo "operation type must be create or delete!!"
fi
