```toml
name = 'update'
method = 'PUT'
url = 'http://localhost:8080/api/v1/ships/1'
sortWeight = 4000000
id = '23739d1a-f13b-4710-a53a-ef4f13834d88'

[body]
type = 'JSON'
raw = '''
{
  "name": "ship modified",
  "showMovie": "show modified"
}'''
```
