wasseryoga
=================



#### CMS- Contentful

- API Call for creating contentypes with custom ids

'''
curl -X PUT -H 'Content-Type: application/vnd.contentful.management.v1+json' -H 'Authorization: Bearer 3d5b3ea16f3922c29317bf4bdde2554e441166ef06d6e289469544fbd24585d0' -d '{"name": "courses","fields":[{"id":"pagecode","name":"pageCode","type":"Symbol"}]}' 'https://api.contentful.com/spaces/5v0ywjti1yhv/content_types/courses'

curl -X GET https://cdn.contentful.com/spaces/cfexampleapi/entries/nyancat?access_token=3db47776a38674de00d8ef2cc307218419f8e673f4a373e8f3e788d2a5cddc8c
