import { Button } from 'react-bootstrap';

import style from './MainButton.module.css';

interface MainButtonProps {
    text: string;
    onClick?: () => void;
    type?: 'button' | 'submit' | 'reset';
}

function MainButton(props: MainButtonProps) {
    const { text, onClick, type } = props;
    return (
        <Button variant={'none'} className={style.myButton} onClick={onClick} type={type}>
            {text}
        </Button>
    );
}

export default MainButton;
