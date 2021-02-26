package com.gift.house.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gift.house.AddProduct;
import com.gift.house.DBHelper;
import com.gift.house.R;
import com.gift.house.models.Order;
import com.gift.house.models.Product;
import com.gift.house.utils.utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    Context context;
    ArrayList<Product> products;
    ArrayList<Product> cartProducts;
    boolean isOrderDetails = false;
    boolean isCart = false;
    int layoutID;
    DBHelper database;
    SharedPreferences sharedPreferences;

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        database = new DBHelper(context);
        sharedPreferences = context.getSharedPreferences("GiftHouse", MODE_PRIVATE);
        cartProducts = new ArrayList<>();
        ArrayList<Order> cart = database.getCart(sharedPreferences.getString("user_phone", ""));
        if (cart != null && cart.size() > 0) {
            cartProducts.addAll(getProducts(cart.get(0).getProducts()));
        }
    }

    public ProductsAdapter(Context context, ArrayList<Product> products, int layoutID, boolean isCart) {
        this.context = context;
        this.products = products;
        this.layoutID = layoutID;
        this.isCart = isCart;
        database = new DBHelper(context);
        sharedPreferences = context.getSharedPreferences("GiftHouse", MODE_PRIVATE);
        cartProducts = new ArrayList<>();
        ArrayList<Order> cart = database.getCart(sharedPreferences.getString("user_phone", ""));
        if (cart != null && cart.size() > 0) {
            cartProducts.addAll(getProducts(cart.get(0).getProducts()));
        }
    }

    public ProductsAdapter(Context context, ArrayList<Product> products, boolean isOrderDetails) {
        this.context = context;
        this.products = products;
        this.isOrderDetails = isOrderDetails;
        database = new DBHelper(context);
        sharedPreferences = context.getSharedPreferences("GiftHouse", MODE_PRIVATE);
        cartProducts = new ArrayList<>();
        ArrayList<Order> cart = database.getCart(sharedPreferences.getString("user_phone", ""));
        if (cart != null && cart.size() > 0) {
            cartProducts.addAll(getProducts(cart.get(0).getProducts()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutID == 0) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_product_admin, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(layoutID, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvCategory.setText(product.getCat_name());
        holder.tvDescription.setText(product.getDescription());
        holder.tvPrice.setText(product.getPrice());

        holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.remove(product);
                String userPhone = sharedPreferences.getString("user_phone", "");
                ArrayList<Order> cart = database.getCart(userPhone);
                if (!userPhone.isEmpty()) {
                    if (cart.size() > 0) {
                        Order order = cart.get(0);
                        ArrayList<Product> products = getProducts(order.getProducts());
                        if (products.size() > 0) {
                            for (Product p : products) {
                                if (p.getId() == product.getId()) {
                                    products.remove(p);
                                    break;
                                }
                            }
                        }
                        if (products.size() < 1) {
                            database.deleteCart(order);
                        } else {
                            Gson gson = new Gson();
                            order.setProducts(gson.toJson(products));
                            order.setAmount(getOrderAmount(products));
                            database.updateCart(order);
                        }
                    }
                }
                notifyItemRemoved(position);
//                notifyDataSetChanged();
            }
        });

        try {
            holder.photo.setImageBitmap(utils.getBitmap(product.getPhoto()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isOrderDetails && layoutID == 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddProduct.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                }
            });
        }

        for (Product p : cartProducts) {
            if (p.getId() == product.getId()) {
                holder.btnAdd.setVisibility(View.INVISIBLE);
                holder.btnRemove.setVisibility(View.VISIBLE);
                break;
            }
        }

        if (isCart) {
            holder.ivClose.setVisibility(View.VISIBLE);
            holder.btnAdd.setVisibility(View.GONE);
            holder.btnRemove.setVisibility(View.GONE);
        } else {
            holder.ivClose.setVisibility(View.GONE);
        }

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(holder.btnAdd, holder.btnRemove, product);
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct(holder.btnAdd, holder.btnRemove, product);
            }
        });
    }

    private void addProduct(ConstraintLayout btnAdd, ConstraintLayout btnRemove, Product product) {
        String userPhone = sharedPreferences.getString("user_phone", "");
        ArrayList<Order> cart = database.getCart(userPhone);
        if (!userPhone.isEmpty()) {
            if (cart.size() > 0) {
                Order order = cart.get(0);
                ArrayList<Product> products = getProducts(order.getProducts());
                if (!products.contains(product)) {
                    products.add(product);
                }
                Gson gson = new Gson();
                order.setProducts(gson.toJson(products));
                order.setAmount(getOrderAmount(products));
                database.updateCart(order);
            } else {
                Order order = new Order();
                order.setOrder_number("GSON" + System.currentTimeMillis());
                order.setUser_phone(userPhone);
                Gson gson = new Gson();
                order.setProducts(gson.toJson((new ArrayList<Product>()).add(product)));
                order.setAmount(getOrderAmount(products));
                database.insertCart(order);
            }
            btnAdd.setVisibility(View.INVISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
        }
    }

    private void removeProduct(ConstraintLayout btnAdd, ConstraintLayout btnRemove, Product product) {
        String userPhone = sharedPreferences.getString("user_phone", "");
        ArrayList<Order> cart = database.getCart(userPhone);
        if (!userPhone.isEmpty()) {
            if (cart.size() > 0) {
                Order order = cart.get(0);
                ArrayList<Product> products = getProducts(order.getProducts());
                if (products.size() > 0) {
                    for (Product p : products) {
                        if (p.getId() == product.getId()) {
                            products.remove(p);
                            break;
                        }
                    }
                }
                if (products.size() < 1) {
                    database.deleteCart(order);
                } else {
                    Gson gson = new Gson();
                    order.setProducts(gson.toJson(products));
                    order.setAmount(getOrderAmount(products));
                    database.updateCart(order);
                }
                btnRemove.setVisibility(View.INVISIBLE);
                btnAdd.setVisibility(View.VISIBLE);
            }
        }
    }

    private String getOrderAmount(ArrayList<Product> products) {
        int sum = 0;
        for (Product product : products) {
            try {
                sum += Integer.parseInt(product.getPrice());
            } catch (NumberFormatException ignore) {
            }
        }
        return String.valueOf(sum);
    }

    private ArrayList<Product> getProducts(String products_) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Product>>() {
            }.getType();
            products.addAll(gson.fromJson(products_, listType));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvCategory, tvPrice;
        ConstraintLayout btnAdd, btnRemove;
        ImageView photo;
        TextView ivClose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvPrice = itemView.findViewById(R.id.tv_price);
            photo = itemView.findViewById(R.id.prodImage);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnRemove = itemView.findViewById(R.id.btn_remove);
            ivClose = itemView.findViewById(R.id.txt_close);
        }
    }
}
