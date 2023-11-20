# 프로젝트 협업 SNS 서비스

<br/>

## Table of Contents

- [개요](#개요)
- [Commit Convention](#Commit Convention)
- [Skils](#skils)
- [Installation](#Installation)
- [Directory](#Directory)
- [API Reference](#api-reference)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [구현과정(설계 및 의도)](<#구현과정(설계-및-의도)>)
- [TIL 및 회고](#til-및-회고)
- [Authors](#authors)
- [References](#references)

<br/>

## 개요

본 서비스는 사용자들이 팀 프로젝트를 모집, 진행, 관리하는데 도움을 주며 SNS 기능을 가진 애플리케이션입니다. 이 앱은 진행중인 팀 프로젝트를 모니터링하고 
사용자들에게 프로젝트, 게시글, 팀원을 추천하여 프로젝트를 쉽고 편하게 진행하고자 하는 목표 달성에 도움이 됩니다.

**(프로젝트, 게시글, 팀원을 추천하는 기능을 통해 원활한 프로젝트 진행을 지원합니다)**


## Commit Convention

```
Feat :	   새로운 기능 추가
Fix : 	   버그 수정
Docs : 	   문서 수정
Style :    코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
Refactor : 코드 리펙토링
Test : 	   테스트(테스트 코드 추가, 수정, 삭제, 비즈니스 로직에 변경이 없는 경우)
Chore :    위에 걸리지 않는 기타 변경사항 (빌드 스크립트 수정, assets image, 패키지 매니저 등)
Design :   CSS 등 사용자 UI 디자인 변경
Comment :  필요한 주석 추가 및 변경
Init :     프로젝트 초기 생성
Rename :   파일 혹은 폴더명 수정하거나 옮기는 경우
Remove :   파일을 삭제하는 작업만 수행하는 경우
```

## Skils

<div align="center">

언어 및 프레임워크 <br/> ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) ![SPRING](https://img.shields.io/badge/spring-6DA55F?style=for-the-badge&logo=spring&logoColor=white) ![JAVA](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white)
<br/>
데이터 베이스 <br/>![Mysql](https://img.shields.io/badge/mysql-%23316192.svg?style=for-the-badge&logo=mysql&logoColor=white)<br/>

</div>

## Installation


```bash
  # 설치
  git clone .git
  
  # 실행
  ./gradlew build -x test
  
  # 파일위치로 이동 후
  javac SynergyBeApplication.java
```


## Directory

<details>
<summary> 파일 구조 보기 </summary>

```
src
├─domain
│   ├─apply
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Apply
│   ├─auth
│   │  ├─controller
│   │  ├─entity
│   │  ├─info
│   │  ├─repository
│   │  ├─service
│   │  └─token
│   ├─comment
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Comment
│   ├─follow
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Follow
│   ├─image
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Image
│   ├─notice
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Notice
│   ├─notification
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Notification
│   ├─post
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Post
│   ├─postlike
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─PostLike
│   ├─project
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Project
│   ├─projectlike
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─ProjectLike
│   ├─projectuser
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─ProjectUser
│   ├─rate
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Rate
│   ├─schedule
│   │  ├─controller
│   │  ├─dto
│   │  ├─repository
│   │  ├─service
│   │  └─Schedule
│   └─user
│      ├─controller
│      ├─dto
│      ├─repository
│      ├─service
│      └─User
└─system
    ├─common
    ├─config
    │  └─properties
    ├─exception
    ├─filter
    ├─handler
    └─utils
```

</details>
<br/>


## API Reference

Swagger : https://synergyy.link/swagger-ui/index.html#/

<details>
<summary>Get all - click</summary>
<img src="./public/full.png" alt="logo" width="80%" />
</details>

<br/>

## 프로젝트 진행 및 이슈 관리

[//]: # ([![Notion]&#40;https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white&#41;]&#40;https://www.notion.so/Team-Careerly-8d62334735154f7f9b9cbba91da21df5&#41;)

[//]: # ([프로젝트 관리 페이지]&#40;https://www.notion.so/Team-Careerly-8d62334735154f7f9b9cbba91da21df5&#41;)

<img src="./public/timeline.png" alt="logo" width="80%" />

<br/>


## 구현과정(설계 및 의도)

ERD

<img src="./public/synergy_db_erd.png" alt="logo" width="80%" />

[budget_management_erd](https://lucid.app/lucidchart/7e0d6cd7-f045-4cf3-a526-aa78cac905e9/edit?invitationId=inv_ad8e0887-5199-422a-9883-4de20341c21e&page=0_0#)

<details>
<summary>entity 설계 시 고려사항- click</summary>

- 주요 도메인으로 user, post, project, notification 으로 나눈다
- Project 와 User 관계
    - Project 와 User는 N 대 N 관계로 설정한다. 이유는 Project(프로젝트)는 User(사용자) 를 여러명 가질 수 있고 반대로 User는 여러 Project를 수행할 수 있으므로
        - 추가로 고려할 사항
            - Project의 요구사항이 늘어남에 따라 User의 정보를 Project 내에서도 양방향 관계로 관리하는게 맞을까 ?
            - 현재는 Project가 여러 User를 가진다는 개념이므로 양방향으로 매핑을 해주자
- Project 와 Category 관계
    - Category를 하나의 Entity로 만들것인가 ?
    - 혹은 Enum으로만 관리할 것 인가 ?
        - Entity로 만들어 관리하자
            - 이유는
            - Project를 카테고리로 나눌 때 테이블로써 관리하면 장점이 많음
                - Project는 주요 도메인이므로 자주 사용되므로 테이블로 관리하면 그만큼 생산성 증가 
</details>

<br/>


## 코드리뷰 및 에러 해결

### 로그인 회원가입

### 맛집 평가
- [맛집평가](https://github.com/Wanted-Internship-Team-Careerly/Location-Based-Foodie-Service/pull/15)
- [맛집상세목록](https://github.com/Wanted-Internship-Team-Careerly/Location-Based-Foodie-Service/pull/35)

<br/>

## Authors

<div align="center">

<br/>

![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) </br>
<a href="https://github.com/rivkode">이종훈</a>

</div>
<br/>

## References

- [Awesome Readme Templates](https://awesomeopensource.com/project/elangosundar/awesome-README-templates)
- [Awesome README](https://github.com/matiassingers/awesome-readme)
- [How to write a Good readme](https://bulldogjob.com/news/449-how-to-write-a-good-readme-for-your-github-project)