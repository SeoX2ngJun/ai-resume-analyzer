import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getDocumentReportApi } from '../api/resumeapi';

const ReportDetail = () => {
  const { documentId } = useParams();
  const navigate = useNavigate();
  const [reportData, setReportData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchReport = async () => {
      try {
        setLoading(true);
        const data = await getDocumentReportApi(documentId);
        setReportData(data);
      } catch (err) {
        setError(err.errorMessage || '리포트를 불러오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    if (documentId) {
      fetchReport();
    }
  }, [documentId]);

  if (loading) return <div style={{ padding: '20px', textAlign: 'center' }}>분석 리포트를 불러오는 중입니다...</div>;
  if (error) return <div style={{ padding: '20px', color: 'red', textAlign: 'center' }}>{error}</div>;
  if (!reportData) return <div style={{ padding: '20px', textAlign: 'center' }}>데이터가 없습니다.</div>;

  const { fileName, aiReport } = reportData;

  return (
    <div style={{ maxWidth: '800px', margin: '40px auto', padding: '20px', border: '1px solid #e0e0e0', borderRadius: '8px' }}>
      <div style={{ borderBottom: '2px solid #333', paddingBottom: '15px', marginBottom: '20px' }}>
        <h2>AI 분석 결과 리포트</h2>
        <p style={{ color: '#666' }}>파일명: {fileName}</p>
        <h3 style={{ color: '#007bff' }}>종합 합격 예측률: {aiReport?.passRate}%</h3>
      </div>

      <div style={{ marginBottom: '20px', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '6px' }}>
        <h4 style={{ margin: '0 0 10px 0' }}>총평 요약</h4>
        <p style={{ margin: 0, lineHeight: '1.6' }}>{aiReport?.summary}</p>
      </div>

      <div style={{ display: 'flex', gap: '20px', marginBottom: '20px' }}>
        <div style={{ flex: 1, padding: '15px', border: '1px solid #c3e6cb', backgroundColor: '#d4edda', borderRadius: '6px' }}>
          <h4 style={{ color: '#155724', margin: '0 0 10px 0' }}>강점 (Strengths)</h4>
          <ul style={{ margin: 0, paddingLeft: '20px', color: '#155724' }}>
            {aiReport?.strengths?.map((item, idx) => (
              <li key={idx} style={{ marginBottom: '5px' }}>{item}</li>
            ))}
          </ul>
        </div>

        <div style={{ flex: 1, padding: '15px', border: '1px solid #f5c6cb', backgroundColor: '#f8d7da', borderRadius: '6px' }}>
          <h4 style={{ color: '#721c24', margin: '0 0 10px 0' }}>약점 (Weaknesses)</h4>
          <ul style={{ margin: 0, paddingLeft: '20px', color: '#721c24' }}>
            {aiReport?.weaknesses?.map((item, idx) => (
              <li key={idx} style={{ marginBottom: '5px' }}>{item}</li>
            ))}
          </ul>
        </div>
      </div>

      <button
        onClick={() => navigate('/')}
        style={{ padding: '10px 20px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
      >
        대시보드로 돌아가기
      </button>
    </div>
  );
};

export default ReportDetail;