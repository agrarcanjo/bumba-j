import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import axios from 'axios';
import './lesson-assignment-form.scss';

interface Lesson {
  id: number;
  title: string;
  description: string;
  language: 'ENGLISH';
  level: 'EASY' | 'INTERMEDIATE' | 'ADVANCED';
  xpReward: string;
  passThreshold: number;
  createdAt: string;
  topic: {
    id: number;
    name: string;
    language: string;
    description: string;
  };
}

export const LessonAssignmentForm = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const classId = searchParams.get('classId');

  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [selectedLesson, setSelectedLesson] = useState<number | null>(null);
  const [dueDate, setDueDate] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadLessons();
  }, []);

  const loadLessons = async () => {
    try {
      setLoading(true);
      const response = await axios.get<Lesson[]>('/api/lessons');
      setLessons(response.data);
    } catch (error) {
      toast.error('Erro ao carregar lições');
      console.error('Error loading lessons:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const classIdStr = searchParams.get('classId');

    if (!classIdStr) {
      toast.error('Turma inválida');
      navigate('/teacher/classes');
      return null;
    }

    if (!selectedLesson) {
      toast.warning('Selecione uma lição');
      return;
    }

    if (!dueDate) {
      toast.warning('Defina uma data de entrega');
      return;
    }

    try {
      setSubmitting(true);
      await axios.post('/api/lesson-assignments', {
        classRoom: { id: parseInt(classIdStr, 10) },
        lesson: { id: selectedLesson },
        dueDate: new Date(dueDate).toISOString(),
      });
      toast.success('Lição atribuída com sucesso!');
      navigate(`/teacher/classes/${classIdStr}`);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Erro ao atribuir lição';
      toast.error(errorMessage);
      console.error('Error assigning lesson:', error);
    } finally {
      setSubmitting(false);
    }
  };

  const filteredLessons = lessons.filter(lesson => {
    const search = searchTerm.toLowerCase();
    return (
      (lesson.title || '').toLowerCase().includes(search) ||
      (lesson.description || '').toLowerCase().includes(search) ||
      (lesson.topic?.name || '').toLowerCase().includes(search)
    );
  });

  const getMinDate = () => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return tomorrow.toISOString().split('T')[0];
  };

  if (loading) {
    return (
      <div className="lesson-assignment-form">
        <div className="loading">Carregando lições...</div>
      </div>
    );
  }

  return (
    <div className="lesson-assignment-form">
      <div className="form-header">
        <h1>Atribuir Lição à Turma</h1>
        <button onClick={() => navigate(`/teacher/classes/${classId}`)} className="btn-back">
          ← Voltar
        </button>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="form-section">
          <label className="form-label">Data de Entrega *</label>
          <input
            type="date"
            value={dueDate}
            onChange={e => setDueDate(e.target.value)}
            min={getMinDate()}
            className="form-input"
            required
          />
          <p className="form-help">A data de entrega deve ser no futuro</p>
        </div>

        <div className="form-section">
          <label className="form-label">Buscar Lição</label>
          <input
            type="text"
            placeholder="Buscar por título, descrição ou tópico..."
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
            className="form-input"
          />
        </div>

        <div className="lessons-selection">
          <div className="selection-header">
            <h3>Selecione uma Lição ({filteredLessons.length})</h3>
          </div>

          {filteredLessons.length === 0 ? (
            <div className="empty-state">
              <p>Nenhuma lição encontrada</p>
            </div>
          ) : (
            <div className="lessons-grid">
              {filteredLessons.map(lesson => (
                <div
                  key={lesson.id}
                  className={`lesson-card ${selectedLesson === lesson.id ? 'selected' : ''}`}
                  onClick={() => setSelectedLesson(lesson.id)}
                >
                  <div className="lesson-header">
                    <h4>{lesson.title}</h4>
                    <span className={`difficulty-badge ${lesson.level.toLowerCase()}`}>{lesson.level}</span>
                  </div>
                  <p className="lesson-description">{lesson.description}</p>
                  <div className="lesson-footer">
                    <span className="lesson-topic">📚 {lesson.topic.name}</span>
                  </div>
                  {selectedLesson === lesson.id && <div className="selected-indicator">✓ Selecionada</div>}
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="form-actions">
          <button type="button" onClick={() => navigate(`/teacher/classes/${classId}`)} className="btn-cancel" disabled={submitting}>
            Cancelar
          </button>
          <button type="submit" className="btn-submit" disabled={submitting || !selectedLesson || !dueDate}>
            {submitting ? 'Atribuindo...' : 'Atribuir Lição'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default LessonAssignmentForm;
