import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Alert } from 'reactstrap';

interface FeedbackModalProps {
  isOpen: boolean;
  correct: boolean;
  explanation?: string;
  correctAnswer?: string;
  onNext: () => void;
}

export const FeedbackModal: React.FC<FeedbackModalProps> = ({ isOpen, correct, explanation, correctAnswer, onNext }) => {
  return (
    <Modal isOpen={isOpen} toggle={onNext} centered>
      <ModalHeader toggle={onNext}>{correct ? '✅ Correto!' : '❌ Incorreto'}</ModalHeader>
      <ModalBody>
        <Alert color={correct ? 'success' : 'danger'} className="mb-3">
          {correct ? 'Parabéns! Você acertou!' : 'Não foi dessa vez, mas continue tentando!'}
        </Alert>
        {!correct && correctAnswer && (
          <div className="mb-3">
            <strong>Resposta correta:</strong> {correctAnswer}
          </div>
        )}
        {explanation && (
          <div>
            <strong>Explicação:</strong>
            <p className="mt-2">{explanation}</p>
          </div>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="primary" onClick={onNext}>
          Próxima Questão
        </Button>
      </ModalFooter>
    </Modal>
  );
};
