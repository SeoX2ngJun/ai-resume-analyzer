//분석 데이터 중앙 창고
// ai 분석중, 분석 결과 데이터, 에러 상태 전역 제어
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { uploadResumeAPI } from '../../api/resumeApi';

export const uploadAndAnalyzeResume = createAsyncThunk(
  'resume/uploadAndAnalyze',
  async (file, { rejectWithValue }) => {
    try {
      const response = await uploadResumeAPI(file);
      return response.data; 
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || '자소서 분석 중 에러가 발생했습니다.'
      );
    }
  }
);

const resumeSlice = createSlice({
  name: 'resume',
  initialState: {
    report: null,
    loading: false,
    error: null,
  },
  reducers: {
    resetResumeState: (state) => {
      state.report = null;
      state.loading = false;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(uploadAndAnalyzeResume.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(uploadAndAnalyzeResume.fulfilled, (state, action) => {
        state.loading = false;
        state.report = action.payload;
      })
      .addCase(uploadAndAnalyzeResume.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export const { resetResumeState } = resumeSlice.actions;
export default resumeSlice.reducer;