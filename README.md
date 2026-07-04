# 🚀 AI 자기소개서 및 문서 분석 서비스

사용자가 PDF 또는 Word 형식의 자기소개서를 업로드하면, AI(OpenAI)를 통해 심층적인 분석 리포트를 제공하는 웹 서비스 프로젝트입니다. 본 저장소는 백엔드(Spring Boot)와 프론트엔드(React)
소스코드를 통합 관리합니다.

---

## 🛠️ 기술 스택 (Tech Stack)

### 💻 Frontend

- **Library**: React
- **Build Tool**: Vite (또는 Create React App 설정에 맞게 수정)
- **State Management**: Context API / Redux Toolkit / Axios

### ☕ Backend

- **Language & Framework**: Java 17 + Spring Boot 3.x
- **Database**: PostgreSQL (Spring Data JPA)
- **Storage**: AWS S3 (사용자 업로드 문서 관리)

### 🌐 Infrastructure & DevOps

- **Container**: Docker
- **Web Server**: Nginx
- **CI/CD**: GitHub Actions (EC2 자동 배포)

---

## 🏗️ 아키텍처 및 협업 핵심 고려사항

1. **대규모 트래픽 및 대용량 파일 처리**
    - PDF/Word 문서 업로드 시 서버 메모리 고갈을 방지하기 위해 파일 크기 제한 및 Multi-part 스트리밍을 최적화합니다.
2. **데이터 무결성 및 트랜잭션 보장**
    - S3 파일 적재, OpenAI 분석 요청, PostgreSQL 결과 저장을 하나의 비즈니스 단위로 묶어 예외 발생 시 완전한 롤백(Rollback)을 보장합니다.
3. **보안 관리 및 환경 변수 분리**
    - AWS Credentials, OpenAI API Key 등 민감한 정보는 소스코드에 하드코딩하지 않고 환경 변수 시스템을 통해 주입합니다.

---

## 📂 프로젝트 구조 (Repository Structure)

```text
├── .github/
│   └── ISSUE_TEMPLATE/     # 공통 이슈 템플릿 (Feature, Bug)
├── frontend/               # React 프론트엔드 프로젝트
│   ├── src/
│   └── package.json
└── backend/                # Spring Boot 백엔드 프로젝트
    ├── src/
    └── build.gradle
