import {useParams} from "react-router";
import {getCoffeeById} from "../services/main.api.ts";
import {Button, Image} from "react-bootstrap";

function ProductPage() {
    const {productId} = useParams();
    const product = getCoffeeById(productId ? parseInt(productId) : 0);

    return (
        <div>
            <div>
                <Image src={product?.imageUrl} alt="product"/>
            </div>
            <div>
                <div>
                    <div>
                    <h3>{product?.origins[0].region}</h3>
                    <h1>{product.name}</h1>
                    </div>
                    <div>
                        <h4>Rating: {product.averageRating} ({product.reviewCount} reviews)</h4>
                    </div>
                </div>
                <div>
                    <h3>{product.price}</h3>
                    <Button>Add to cart</Button>
                </div>
                <div>
                    <p>{product.description}</p>
                </div>
            </div>
        </div>
    )
}

export default ProductPage;
