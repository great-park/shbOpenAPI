# 외부제휴 Open API 테스트용

### Access Token 발급

> ![](https://img.shields.io/static/v1?label=&message=POST&color=brightgreen) <br>
> localhost:8080/**token**/**partner**

<details markdown="1">
<summary>detail</summary>

토큰 발급 테스트

#### Parameters

##### Body

|     name     |  type  |    description     | required |
|:------------:|:------:|:------------------:|:--------:|
|   clientId   | String | 클라이언트 id (API KEY) | Required |
| clientSecret | String |    클라이언트 Secret    | Required |

#### Response

```json
{
    "success": true,
    "responseCode": 0,
    "message": "Ok",
    "data": {
        "accessToken": "Bearer xxxxxxxxxxxxxxxxxxxx~~",
        "expiresIn": "86400"
    }
}
```

</details>
<br>






### Open API 테스트

> ![](https://img.shields.io/static/v1?label=&message=POST&color=brightgreen) <br>
> localhost:8080/**"테스트할 Open API URI"**

<details markdown="1">
<summary>detail</summary>


#### Parameters

##### Body

|     name      | type |    description     | required |
|:-------------:| :---: |:------------------:| :---: |
| Authorization | string |    Access Token    | **Required** |
|   ClientId    | string | 클라이언트 Id (API KEY) | **Required** |
| ClientSecret  | string |    클라이언트 Secret    | **Required** |

##### Body

각 Open API의 Body값 그대로 넣으면 됩니다.

</details>
<br>


### Body Hash

> ![](https://img.shields.io/static/v1?label=&message=POST&color=brightgreen) <br>
> localhost:8080/**token**/**body**

<details markdown="1">
<summary>detail</summary>



#### Parameters

##### Body

|     name     |  type  |    description     | required |
|:------------:|:------:|:------------------:|:--------:|
|   clientId   | String | 클라이언트 id (API KEY) | Required |
| clientSecret | String |    클라이언트 Secret    | Required |

#### Response

```json
{
    "success": true,
    "responseCode": 0,
    "message": "Ok",
    "data": {
        "accessToken": "Bearer xxxxxxxxxxxxxxxxxxxx~~",
        "expiresIn": "86400"
    }
}
```

</details>
<br>
