import { Modal } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router';

import type { FormValues } from '../../pages/CartCheckoutPage.tsx';
import MainButton from '../common/MainButton.tsx';

interface CheckoutModalProps {
    show: boolean;
    onHide: () => void;
    formValues?: FormValues;
    clearCart: () => void;
    setShowSuccessfulOrder: (show: boolean) => void;
}

function CheckoutModal(props: CheckoutModalProps) {
    const { onHide, clearCart, formValues, setShowSuccessfulOrder, show } = props;
    const { t } = useTranslation();
    const navigate = useNavigate();
    const handleConfirm = () => {
        setShowSuccessfulOrder(true);
        setTimeout(() => {
            clearCart();
            void navigate('/');
        }, 5000);
        onHide();
    };

    return (
        <Modal onHide={onHide} show={show} size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">{t('checkout.reviewOrder')}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
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
                <MainButton text={t('checkout.sendOrder')} onClick={handleConfirm} />
            </Modal.Footer>
        </Modal>
    );
}

export default CheckoutModal;
