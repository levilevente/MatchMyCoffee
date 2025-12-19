import {Container, Image, Nav, Navbar} from 'react-bootstrap';
import {useTranslation} from 'react-i18next';
import 'bootstrap/dist/css/bootstrap.min.css';
import {FaCartShopping} from "react-icons/fa6";
import {FaHeart} from "react-icons/fa";


import style from './NavigationBar.module.css';

function NavigationBar() {
    const {t} = useTranslation();

    return (
        <Navbar expand="lg" className={`${style.navbarStyle}`}>
            <Container className={style.gridContainer}>
                <Navbar.Brand href="/" className={style.brandCentered}>
                    <Image src="/logo/logo.png" alt="Logo" className={style.logoStyle}/>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className={`ms-auto ${style.navStyle}`}>
                        <Nav.Link href="/blog" className={style.navLinkStyle}>
                            {t('blogposts.title')}
                        </Nav.Link>
                        <Nav.Link href="/favorites" className={style.navLinkStyle}>
                            <FaHeart size={20}/>
                        </Nav.Link>
                        <Nav.Link href="/cart" className={style.navLinkStyle}>
                            <FaCartShopping size={20}/>
                        </Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavigationBar;
