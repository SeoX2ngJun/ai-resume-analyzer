import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
import ResumeUpload from './components/ResumeUpload';

// 1. 임시 메인 대시보드 컴포넌트 (추후 분리 예정)
const MainDashboard = () => {
  const navigate = useNavigate();
  return (
    <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
      <h2>AI 자기소개서 분석 서비스</h2>
      <button onClick={() => navigate('/upload')} style={{ padding: '10px 20px', cursor: 'pointer' }}>
        새 자소서 분석하기
      </button>
      <div style={{ marginTop: '20px', padding: '20px', backgroundColor: '#f5f5f5' }}>
        <p>추후 이 곳에 과거 분석 내역 리스트가 렌더링됩니다.</p>
      </div>
    </div>
  );
};

// 2. 임시 AI 리포트 결과 컴포넌트 (추후 분리 및 API 연동 예정)
const ReportDetail = () => {
  return (
    <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
      <h2>AI 분석 결과 리포트 상세 페이지</h2>
      <p>분석이 완료된 결과 데이터가 이곳에 표시됩니다.</p>
      <Link to="/">메인으로 돌아가기</Link>
    </div>
  );
};

// 3. 중앙 라우팅 컨트롤러
const App = () => {
  return (
    <Router>
      <Routes>
        {/* 접속 시 기본 화면 */}
        <Route path="/" element={<MainDashboard />} />
        
        {/* 우리가 만든 자소서 업로드 화면 */}
        <Route path="/upload" element={<ResumeUpload />} />
        
        {/* 분석 완료 후 이동할 결과 상세 화면 (URL 파라미터로 documentId 수신) */}
        <Route path="/report/:documentId" element={<ReportDetail />} />
      </Routes>
    </Router>
  );
};

export default App;