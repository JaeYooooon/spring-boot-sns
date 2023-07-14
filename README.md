# 📍 Social Network Service

유저들이 소통할 수 있는 소셜 네트워크 서비스입니다.

## 📌 프로젝트 기능 및 설계

- 회원가입 기능
    - 사용자는 회웝가입을 할 수 있습니다.
    - 회원가입 시 아이디와 패스워드, 닉네임을 입력해야 하며, 아이디와 닉네임은 고유해야 합니다.

- 로그인 기능
    - 사용자는 로그인을 할 수 있습니다. 로그인 시 회원가입 때 사용한 아이디와 패스워드가 일치해야 합니다.
    - 로그인이 완료되면 JSON Web Token 이 발급됩니다.

- 회원
    - 사용자는 회원가입 시 작성한 회원 정보를 조회할 수 있습니다.
    - 사용자는 회원가입 시 작성한 회원 정보를 수정할 수 있습니다.
    - 계정의 공개 범위를 설정할 수 있습니다 (공개/비공개).
    - 사용자는 본인의 팔로우/팔로잉 목록을 조회할 수 있습니다.
    - 사용자는 닉네임으로 다른 사용자를 검색할 수 있습니다.

- 팔로우
    - 사용자는 다른 사용자를 팔로우/언팔로우할 수 있습니다.
    - 비공개 계정인 사용자를 팔로우하려는 경우, 상대방이 팔로우 요청을 수락하거나 거절할 수 있습니다.

- 게시글
    - 사용자는 게시글을 작성/조회/수정/삭제 할 수 있습니다.
    - 게시글이 삭제되면 해당 게시글의 댓글과 대댓글이 모두 삭제됩니다.
    - 사용자는 게시글 제목(텍스트), 게시글 내용(텍스트)를 작성/수정 할 수 있습니다.
    - 게시글은 종류가 많을 수 있으므로 페이징 처리를 합니다.
    - 게시글 목록 조회시 응답에는 게시글 제목과 작성일, 댓글 수의 정보가 필요합니다.
    - 사용자는 게시글에 "좋아요"를 누를 수 있습니다.
    - 게시글은 최신순으로 정렬됩니다.
    - 팔로잉 하는 사용자들의 게시글을 조회할 수 있습니다.
    - 사용자는 게시글에 다른 사람을 태그 할 수 있습니다.
    - 사용자는 게시글에 태그되면 알림을 받습니다.

- 댓글
    - 사용자는 특정 게시글 조회 시 댓글목록도 함께 조회됩니다.
    - 사용자는 게시글에 댓글을 작성/삭제 할 수 있습니다.
    - 댓글 목록 조회시에는 댓글 작성자와 댓글 내용, 댓글 작성일의 정보가 필요합니다.
    - 댓글은 최신순으로 정렬됩니다.
    - 대댓글을 작성/삭제 할 수 있습니다.

- 스토리
    - 사용자는 스토리를 작성, 조회, 삭제할 수 있습니다.
    - 사용자는 스토리 사진, 텍스트를 작성할 수 있습니다.
    - 스토리는 24시간 뒤 자동으로 삭제됩니다.
    - 사용자는 스토리를 조회한 사람의 목록을 조회할 수 있습니다.
    - 팔로우하는 사용자들의 스토리를 조회할 수 있습니다.

- 메세지
    - 사용자는 메세지를 전송/조회 할 수 있습니다.
    - 사용자는 더 이상 필요하지 않은 메세지를 삭제할 수 있습니다.

## 🧾 ERD
![img.png](docs/ERD/erd.png)

## 🛠 Trouble Shooting

[go to the trouble shooting section](docs/TROUBLE_SHOOTING.md)

## ⚙ Tech Stack

<div> 
  <img src="https://img.shields.io/badge/Java-181717?style=for-the-badge&logo=Conda-Forge&logoColor=white"> 
  <img src="https://img.shields.io/badge/SpringBoot-181717?style=for-the-badge&logo=SpringBoot&logoColor=white"> 
  <img src="https://img.shields.io/badge/MySQL-181717?style=for-the-badge&logo=MySql&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon S3-181717?style=for-the-badge&logo=amazonaws&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
</div>