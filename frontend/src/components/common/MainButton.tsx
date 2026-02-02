import type { ReactNode } from 'react';
import { Button } from 'react-bootstrap';

import style from './MainButton.module.css';

interface MainButtonProps {
    text: ReactNode;
    onClick?: () => void;
    type?: 'button' | 'submit' | 'reset';
    disabled?: boolean;
}

function MainButton(props: MainButtonProps) {
    const { text, onClick, type, disabled } = props;
    return (
        <Button variant={'none'} className={style.myButton} onClick={onClick} type={type} disabled={disabled}>
            {text}
        </Button>
    );
}

export default MainButton;
