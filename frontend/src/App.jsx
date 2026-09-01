import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

// 분리된 3개의 핵심 화면 컴포넌트 Import
import MainDashboard from './components/MainDashboard';
import ResumeUpload from './components/ResumeUpload';
import ReportDetail from './components/ReportDetail';

const App = () => {
  return (
    <Router>
      <Routes>
        {/* 1. 접속 시 기본 화면: 과거 내역 대시보드 */}
        <Route path="/" element={<MainDashboard />} />
        
        {/* 2. 자소서 업로드 화면 */}
        <Route path="/upload" element={<ResumeUpload />} />
        
        {/* 3. 분석 완료 후 이동할 결과 상세 화면 (URL 파라미터로 documentId 수신) */}
        <Route path="/report/:documentId" element={<ReportDetail />} />
      </Routes>
    </Router>
  );
};

export default App;