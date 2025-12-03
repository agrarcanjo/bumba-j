import React, { useState, useEffect } from 'react';
import { FormGroup, Label, Input, Button } from 'reactstrap';
import { IQuestion } from '../lesson.service';

interface QuestionRendererProps {
  question: IQuestion;
  onSubmit: (answer: string) => void;
  disabled: boolean;
}

export const QuestionRenderer: React.FC<QuestionRendererProps> = ({ question, onSubmit, disabled }) => {
  const [answer, setAnswer] = useState('');
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);

  useEffect(() => {
    setAnswer('');
    setSelectedIndex(null);
  }, [question.id]);

  const handleSubmit = () => {
    let formattedAnswer = '';

    switch (question.type) {
      case 'MULTIPLE_CHOICE':
        if (selectedIndex !== null) {
          formattedAnswer = JSON.stringify({ selected_index: selectedIndex });
        }
        break;
      case 'FILL_BLANK':
      case 'LISTENING':
      case 'READING':
      case 'WRITING':
        if (answer.trim()) {
          formattedAnswer = JSON.stringify({ text: answer.trim() });
        }
        break;
      default:
        formattedAnswer = answer;
    }

    if (formattedAnswer) {
      onSubmit(formattedAnswer);
    }
  };

  const handleOptionSelect = (index: number) => {
    setSelectedIndex(index);
  };

  const renderMultipleChoice = () => (
    <FormGroup>
      {question.parsedContent?.options?.map((option, index) => (
        <div key={index} className="mb-2">
          <Label check>
            <Input
              type="radio"
              name="answer"
              value={index}
              checked={selectedIndex === index}
              onChange={() => handleOptionSelect(index)}
              disabled={disabled}
            />{' '}
            {option}
          </Label>
        </div>
      ))}
    </FormGroup>
  );

  const renderFillBlank = () => (
    <FormGroup>
      <Label className="mb-3">{question.parsedContent?.sentence}</Label>
      <Input
        type="text"
        value={answer}
        onChange={e => setAnswer(e.target.value)}
        placeholder="Digite sua resposta..."
        disabled={disabled}
      />
    </FormGroup>
  );

  const renderListening = () => (
    <div>
      {question.parsedContent?.audioUrl && (
        <div className="mb-3">
          <audio controls className="w-100">
            <source src={question.parsedContent.audioUrl} type="audio/mpeg" />
            Seu navegador não suporta o elemento de áudio.
          </audio>
        </div>
      )}
      <FormGroup>
        <Input
          type="text"
          value={answer}
          onChange={e => setAnswer(e.target.value)}
          placeholder="Digite o que você ouviu..."
          disabled={disabled}
        />
      </FormGroup>
    </div>
  );

  const renderReading = () => (
    <div>
      {question.parsedContent?.imageUrl && (
        <div className="mb-3 text-center">
          <img src={question.parsedContent.imageUrl} alt="Reading material" className="img-fluid" style={{ maxHeight: '300px' }} />
        </div>
      )}
      <FormGroup>
        <Input
          type="textarea"
          value={answer}
          onChange={e => setAnswer(e.target.value)}
          placeholder="Digite sua resposta..."
          rows={4}
          disabled={disabled}
        />
      </FormGroup>
    </div>
  );

  const renderWriting = () => (
    <FormGroup>
      <Input
        type="textarea"
        value={answer}
        onChange={e => setAnswer(e.target.value)}
        placeholder="Escreva sua resposta..."
        rows={6}
        disabled={disabled}
      />
    </FormGroup>
  );

  const renderSpeaking = () => (
    <div className="text-center p-4 bg-light rounded">
      <p className="text-muted">🎤 Funcionalidade de fala em desenvolvimento</p>
      <small>Em breve você poderá gravar sua resposta</small>
    </div>
  );

  const renderQuestion = () => {
    switch (question.type) {
      case 'MULTIPLE_CHOICE':
        return renderMultipleChoice();
      case 'FILL_BLANK':
        return renderFillBlank();
      case 'LISTENING':
        return renderListening();
      case 'READING':
        return renderReading();
      case 'WRITING':
        return renderWriting();
      case 'SPEAKING':
        return renderSpeaking();
      default:
        return <p>Tipo de questão não suportado</p>;
    }
  };

  const isAnswerValid = () => {
    if (question.type === 'MULTIPLE_CHOICE') {
      return selectedIndex !== null;
    }
    return answer.trim().length > 0;
  };

  return (
    <div>
      <h4 className="mb-4">{question.prompt}</h4>
      {renderQuestion()}
      {question.type !== 'SPEAKING' && (
        <Button color="primary" onClick={handleSubmit} disabled={disabled || !isAnswerValid()} className="mt-3">
          Enviar Resposta
        </Button>
      )}
    </div>
  );
};
