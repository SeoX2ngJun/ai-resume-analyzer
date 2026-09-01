import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getDocumentListApi } from '../api/resumeapi';

const MainDashboard = () => {
  const navigate = useNavigate();
  const [documentList, setDocumentList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchList = async () => {
      try {
        setLoading(true);
        // 백엔드 API 호출 시도
        const data = await getDocumentListApi();
        setDocumentList(data);
      } catch (err) {
        setError(err.errorMessage || '서버 통신에 실패했습니다. 백엔드(8080) 구동 상태를 확인해주세요.');
      } finally {
        setLoading(false);
      }
    };

    fetchList();
  }, []);

  return (
    <div style={{ maxWidth: '850px', margin: '50px auto', fontFamily: 'system-ui, sans-serif' }}>
      {/* 상단 헤더 영역 */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <div>
          <h2 style={{ margin: '0 0 8px 0', color: '#1a1a1a', fontSize: '24px' }}>AI 자기소개서 대시보드</h2>
          <p style={{ margin: 0, color: '#666', fontSize: '14px' }}>분석이 완료된 자기소개서 이력을 관리하고 새로운 리포트를 요청하세요.</p>
        </div>
        <button
          onClick={() => navigate('/upload')}
          style={{
            padding: '12px 24px',
            backgroundColor: '#0056b3',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            fontSize: '15px',
            fontWeight: '600',
            cursor: 'pointer',
            boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
            transition: 'background-color 0.2s'
          }}
          onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#004494'}
          onMouseOut={(e) => e.currentTarget.style.backgroundColor = '#0056b3'}
        >
          + 새 자소서 분석하기
        </button>
      </div>

      {/* 데이터 리스트 컨테이너 */}
      <div style={{
        backgroundColor: '#fff',
        border: '1px solid #eaeaea',
        borderRadius: '10px',
        boxShadow: '0 4px 6px rgba(0,0,0,0.02)',
        overflow: 'hidden'
      }}>
        <div style={{ padding: '20px 24px', backgroundColor: '#f8f9fa', borderBottom: '1px solid #eaeaea' }}>
          <h3 style={{ margin: 0, fontSize: '16px', color: '#333' }}>과거 분석 내역</h3>
        </div>

        <div style={{ padding: '24px' }}>
          {/* 상태별 UI 분기 처리 */}
          {loading && (
            <div style={{ textAlign: 'center', padding: '40px 0', color: '#666' }}>
              <div style={{ marginBottom: '10px' }}>⏳</div>
              데이터를 동기화하고 있습니다...
            </div>
          )}

          {error && (
            <div style={{ 
              padding: '20px', 
              backgroundColor: '#fff5f5', 
              border: '1px solid #feb2b2', 
              borderRadius: '6px',
              color: '#c53030',
              textAlign: 'center'
            }}>
              <span style={{ fontWeight: 'bold', display: 'block', marginBottom: '8px' }}>⚠️ 네트워크 오류</span>
              {error}
            </div>
          )}

          {!loading && !error && documentList.length === 0 && (
            <div style={{ textAlign: 'center', padding: '40px 0', color: '#888' }}>
              📭 아직 분석된 자기소개서가 없습니다.<br/>우측 상단의 버튼을 눌러 첫 분석을 시작해보세요.
            </div>
          )}

          {!loading && !error && documentList.length > 0 && (
            <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
              {documentList.map((doc) => (
                <li
                  key={doc.documentId}
                  onClick={() => navigate(`/report/${doc.documentId}`)}
                  style={{
                    padding: '16px 20px',
                    marginBottom: '12px',
                    border: '1px solid #eee',
                    borderRadius: '8px',
                    cursor: 'pointer',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    transition: 'all 0.2s ease',
                  }}
                  onMouseOver={(e) => {
                    e.currentTarget.style.borderColor = '#b3d4fc';
                    e.currentTarget.style.backgroundColor = '#f0f7ff';
                  }}
                  onMouseOut={(e) => {
                    e.currentTarget.style.borderColor = '#eee';
                    e.currentTarget.style.backgroundColor = 'transparent';
                  }}
                >
                  <div>
                    <span style={{ fontWeight: '600', display: 'block', color: '#2c3e50', marginBottom: '4px' }}>
                      📄 {doc.fileName}
                    </span>
                    <span style={{ fontSize: '13px', color: '#888' }}>업로드 일자: {doc.createdAt}</span>
                  </div>
                  <div style={{ color: '#0056b3', fontSize: '14px', fontWeight: '500' }}>
                    리포트 보기 ➔
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
};

export default MainDashboard;