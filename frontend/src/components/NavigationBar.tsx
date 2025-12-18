import { Container, Nav, Navbar } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import style from './NavigationBar.module.css';

function NavigationBar() {
    const { t } = useTranslation();

    return (
        <Navbar expand="lg" data-bs-theme="dark" className={style.navbarStyle}>
            <Container className={style.gridContainer}>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className={`ms-auto ${style.navStyle}`}>
                        <Nav.Link href="/blog" className={style.navLinkStyle}>
                            {t('blogposts.title')}
                        </Nav.Link>
                        <Nav.Link href="/favorites" className={style.navLinkStyle}>
                            {t('favorites.title')}
                        </Nav.Link>
                        <Nav.Link href="/home" className={style.navLinkStyle}>
                            {t('home.title')}
                        </Nav.Link>
                        <Nav.Link href="/cart" className={style.navLinkStyle}>
                            {t('cart.title')}
                        </Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavigationBar;
