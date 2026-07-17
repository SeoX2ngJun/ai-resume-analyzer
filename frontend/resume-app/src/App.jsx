import React from 'react';
import ResumeUpload from './components/ResumeUpload';

function App() {
  return (
    <div style={{ minHeight: '100vh', backgroundColor: '#f5f5f5', padding: '20px' }}>
      <header style={{ borderBottom: '1px solid #eaeaea', paddingBottom: '10px', marginBottom: '20px', textAlign: 'center' }}>
        <h1 style={{ margin: 0, color: '#333' }}>AI 문서 분석 서비스</h1>
      </header>
      <main>
        <ResumeUpload />
      </main>
    </div>
  );
}

export default App;