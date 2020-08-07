import AuthenticationReducer from "./Reducers/AuthenticationReducer";
import { createStore, applyMiddleware } from "redux";
import { persistStore, persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage"; // defaults to localStorage for web
import logger from "redux-logger";
import thunk from "redux-thunk";

const persistConfig = {
  key: "root",
  storage,
};

const persistedReducer = persistReducer(persistConfig, AuthenticationReducer);

export const store = createStore(
  persistedReducer,
  {},
  applyMiddleware(logger, thunk)
);

export const persistor = persistStore(store);
