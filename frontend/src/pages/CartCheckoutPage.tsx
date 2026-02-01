import React, { useContext, useState } from 'react';
import { Alert, Dropdown, DropdownButton, Form } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import CheckoutModal from '../components/cart/CheckoutModal.tsx';
import MainButton from '../components/common/MainButton.tsx';
import ProductGrid from '../components/common/ProductGrid.tsx';
import { CartContext } from '../context/CartContext.tsx';
import style from './CartCheckoutPage.module.css';

export interface FormValues {
    email: string;
    firstName: string;
    lastName: string;
    addressLine1: string;
    addressLine2?: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
    paymentMethod: string;
    totalAmount: number;
}

function CartCheckoutPage() {
    const { cartItems, clearCart } = useContext(CartContext);
    const [paymentMethod, setPaymentMethod] = useState('cashOnDelivery');
    const [modalShow, setModalShow] = useState(false);
    const [showSuccessfulOrder, setShowSuccessfulOrder] = useState(false);

    const [formValues, setFormValues] = useState<FormValues>({
        email: '',
        firstName: '',
        lastName: '',
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        zipCode: '',
        country: '',
        paymentMethod: 'cashOnDelivery',
        totalAmount: 0,
    });

    const [validated, setValidated] = useState(false);

    const { t } = useTranslation();

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        } else {
            event.preventDefault();
            const formData = new FormData(form);

            // 1. Extract Data
            const updatedFormValues: FormValues = {
                email: formData.get('formBasicEmail') as string,
                firstName: formData.get('formBasicFirstName') as string,
                lastName: formData.get('formBasicLastName') as string,
                addressLine1: formData.get('formBasicAddressLine1') as string,
                addressLine2: formData.get('formBasicAddressLine2') as string,
                city: formData.get('formBasicCity') as string,
                state: formData.get('formBasicState') as string,
                zipCode: formData.get('formBasicZip') as string,
                country: formData.get('formBasicCountry') as string,
                paymentMethod: paymentMethod,
                totalAmount: cartItems.reduce((total, item) => total + item.product.price * item.quantity, 0),
            };
            setFormValues(updatedFormValues);
            setModalShow(true);
        }

        setValidated(true);
        // Send status update to backend 
    };

    if (showSuccessfulOrder) {
        return (
            <div className={style.container}>
                <Alert key={'success'} variant={'success'}>
                    {t('checkout.thankYouMessage')}
                </Alert>
            </div>
        );
    }

    return (
        <div className={style.container}>
            <div className={style.leftContainer}>
                <Form noValidate validated={validated} onSubmit={handleSubmit}>
                    <h1>{t('checkout.title')}</h1>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>{t('checkout.email')}</Form.Label>
                        <Form.Control
                            name="formBasicEmail"
                            required
                            type="email"
                            placeholder={t('checkout.emailPlaceholder')}
                        />
                        <Form.Control.Feedback type="invalid">
                            {t('checkout.emailRequiredError') || 'Email is required'}
                        </Form.Control.Feedback>
                        <Form.Text className="text-muted">{t('checkout.emailHelpText')}</Form.Text>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicFirstName">
                        <Form.Label>{t('checkout.firstName')}</Form.Label>
                        <Form.Control
                            name="formBasicFirstName"
                            required
                            type="text"
                            placeholder={t('checkout.firstNamePlaceholder')}
                        />
                        <Form.Control.Feedback type="invalid">Required</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicLastName">
                        <Form.Label>{t('checkout.lastName')}</Form.Label>
                        <Form.Control
                            name="formBasicLastName"
                            required
                            type="text"
                            placeholder={t('checkout.lastNamePlaceholder')}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicAddressLine1">
                        <Form.Label>{t('checkout.addressLine1')}</Form.Label>
                        <Form.Control
                            name="formBasicAddressLine1"
                            required
                            type="text"
                            placeholder={t('checkout.addressLine1Placeholder')}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicAddressLine2">
                        <Form.Label>{t('checkout.addressLine2')}</Form.Label>
                        <Form.Control
                            name="formBasicAddressLine2"
                            type="text"
                            placeholder={t('checkout.addressLine2Placeholder')}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicCity">
                        <Form.Label>{t('checkout.city')}</Form.Label>
                        <Form.Control
                            name="formBasicCity"
                            required
                            type="text"
                            placeholder={t('checkout.cityPlaceholder')}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicState">
                        <Form.Label>{t('checkout.state')}</Form.Label>
                        <Form.Control
                            name="formBasicState"
                            required
                            type="text"
                            placeholder={t('checkout.statePlaceholder')}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicZip">
                        <Form.Label>{t('checkout.zipCode')}</Form.Label>
                        <Form.Control
                            name="formBasicZip"
                            required
                            type="text"
                            placeholder={t('checkout.zipCodePlaceholder')}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicCountry">
                        <Form.Label>{t('checkout.country')}</Form.Label>
                        <Form.Control
                            name="formBasicCountry"
                            required
                            type="text"
                            placeholder={t('checkout.countryPlaceholder')}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicPaymentMethod">
                        <Form.Label>{t('checkout.paymentMethod')}</Form.Label>
                        <DropdownButton
                            title={t(`checkout.${paymentMethod}`)}
                            onSelect={(eventKey) => {
                                if (eventKey) setPaymentMethod(eventKey);
                            }}
                            variant={'outline-dark'}
                        >
                            <Dropdown.Item eventKey="creditCard">{t('checkout.creditCard')}</Dropdown.Item>
                            <Dropdown.Item eventKey="paypal">{t('checkout.paypal')}</Dropdown.Item>
                            <Dropdown.Item eventKey="cashOnDelivery">{t('checkout.cashOnDelivery')}</Dropdown.Item>
                        </DropdownButton>
                    </Form.Group>
                    <MainButton text={t('checkout.reviewOrder')} type="submit" />
                </Form>
                <CheckoutModal
                    show={modalShow}
                    onHide={() => setModalShow(false)}
                    formValues={formValues}
                    clearCart={clearCart}
                    setShowSuccessfulOrder={setShowSuccessfulOrder}
                />
            </div>
            <div className={style.rightContainer}>
                <ProductGrid products={cartItems} inCart />
            </div>
        </div>
    );
}

export default CartCheckoutPage;
