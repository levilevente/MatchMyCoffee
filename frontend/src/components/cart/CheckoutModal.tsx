import { useState } from 'react';
import { Alert, Modal, Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router';

import type { FormValues } from '../../pages/CartCheckoutPage.tsx';
import { updateOrderStatus } from '../../services/main.api.ts';
import { type Order, OrderStatus } from '../../types/OrderType.ts';
import MainButton from '../common/MainButton.tsx';

interface CheckoutModalProps {
    show: boolean;
    onHide: () => void;
    formValues?: FormValues;
    clearCart: () => void;
    setShowSuccessfulOrder: (show: boolean) => void;
    order: Order | null;
}

function CheckoutModal(props: CheckoutModalProps) {
    const { onHide, clearCart, formValues, setShowSuccessfulOrder, show, order } = props;
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleConfirm = async () => {
        if (!order) return;

        setIsSubmitting(true);
        setError(null);

        try {
            // Update order status to PROCESSING
            await updateOrderStatus(order.id, OrderStatus.PROCESSING);

            setShowSuccessfulOrder(true);
            setTimeout(() => {
                clearCart();
                void navigate('/');
            }, 5000);
            onHide();
        } catch (err) {
            console.error('Failed to submit order:', err);
            const errorMessage = (err as { response?: { data?: { error?: string } } })?.response?.data?.error;
            setError(errorMessage ?? t('checkout.orderSubmitError') ?? 'Failed to submit order. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleCancel = () => {
        onHide();
    };

    return (
        <Modal onHide={handleCancel} show={show} size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">{t('checkout.reviewOrder')}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {error ? (
                    <Alert variant="danger" onClose={() => setError(null)} dismissible>
                        {error}
                    </Alert>
                ) : null}
                {formValues ? (
                    <div>
                        <h5>{t('checkout.shippingInformation')}</h5>
                        <p>{formValues.addressLine1}</p>
                        {formValues.addressLine2 ? <p>{formValues.addressLine2}</p> : null}
                        <p>
                            {formValues.city}, {formValues.state} {formValues.zipCode}
                        </p>
                        <p>{formValues.country}</p>

                        <h5>{t('checkout.paymentMethod')}</h5>
                        <p>{t(`checkout.${formValues.paymentMethod}`)}</p>

                        <h5>{t('checkout.totalAmount')}</h5>
                        <p>{formValues.totalAmount} $</p>
                    </div>
                ) : null}
            </Modal.Body>
            <Modal.Footer>
                <MainButton
                    text={
                        isSubmitting ? (
                            <>
                                <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />{' '}
                                {t('checkout.submitting') || 'Submitting...'}
                            </>
                        ) : (
                            t('checkout.sendOrder')
                        )
                    }
                    onClick={() => void handleConfirm()}
                    disabled={isSubmitting}
                />
            </Modal.Footer>
        </Modal>
    );
}

export default CheckoutModal;
